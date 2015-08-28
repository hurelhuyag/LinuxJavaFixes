package net.x11.patch;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;

import java.io.ByteArrayInputStream;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import java.util.Properties;

/**
 * Created by mikl on 08.02.14
 */
public class XKeysymTransformer implements ClassFileTransformer {
    public static final String XNET_PROTOCOL = "sun/awt/X11/XKeysym";
    public static final String PATCH_KEY_MAPPING_PROPERTIES = "mn.properties";
    public static final String PRINT = "print";
    private String agentArgument;

    public XKeysymTransformer(String agentArgument) {
        this.agentArgument = agentArgument;
    }

    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        try {
            return className!=null && className.equals(XNET_PROTOCOL)
                    ? doClass(className, classBeingRedefined, classfileBuffer)
                    : classfileBuffer;
        } catch (Throwable err) {
            err.printStackTrace();
            return classfileBuffer;
        }
    }

    private byte[] doClass(String name, Class clazz, byte[] b) {
        CtClass cl = null;
        ClassPool pool = ClassPool.getDefault();
        try {
            cl = pool.makeClass(new ByteArrayInputStream(b));
            if(agentArgument!=null && agentArgument.equals(PRINT)) {
                CtMethod method = cl.getDeclaredMethod("getJavaKeycode");
                method.insertBefore("System.out.println(\"LinuxJavaPatchAgent.keysym=\"+Long.toHexString($1));");
            } else {
                Properties props = LinuxJavaPatchAgent.getProperties(PATCH_KEY_MAPPING_PROPERTIES, agentArgument);
                for(Object key: props.keySet()) {
                    String value = props.getProperty((String) key);
                    cl.getClassInitializer().insertAfter("keysym2JavaKeycodeHash.put( Long.valueOf(0x"+key+"l), new sun.awt.X11.XKeysym$Keysym2JavaKeycode(java.awt.event.KeyEvent.VK_"+value+", java.awt.event.KeyEvent.KEY_LOCATION_STANDARD));");
                }
            }
            b = cl.toBytecode();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("LinuxJavaPatchAgent.Could not instrument  " + name + ",  exception : " + e.getMessage());
        } finally {
            if (cl != null) {
                cl.detach();
            }
        }
        return b;
    }

}
