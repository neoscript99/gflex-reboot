package ns.gflex.util

import java.security.MessageDigest

import sun.misc.BASE64Encoder;

/**
 * Functions
 * @since Dec 22, 2010
 * @author wangchu
 */
class EncoderUtil {
    static final BASE64Encoder base64Encoder = new BASE64Encoder();
    /**
     * @param inputString
     * @return encoded string
     */
    static public String md5(String inputString) {
        MessageDigest digest = MessageDigest.getInstance('MD5');
        digest.update(inputString.getBytes());
        return base64Encoder.encode(digest.digest());
    }
}
