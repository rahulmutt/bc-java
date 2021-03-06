package org.bouncycastle.mime.smime;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.cms.CMSAlgorithm;
import org.bouncycastle.util.Strings;

class SMimeUtils
{


    private static final Map RFC5751_MICALGS;
    private static final Map RFC3851_MICALGS;
    private static final Map STANDARD_MICALGS;
    private static final Map forMic;

    private static final byte[] nl = new byte[2];


    static
    {
        nl[0] = '\r';
        nl[1] = '\n';


        Map stdMicAlgs = new HashMap();

        stdMicAlgs.put(CMSAlgorithm.MD5, "md5");
        stdMicAlgs.put(CMSAlgorithm.SHA1, "sha-1");
        stdMicAlgs.put(CMSAlgorithm.SHA224, "sha-224");
        stdMicAlgs.put(CMSAlgorithm.SHA256, "sha-256");
        stdMicAlgs.put(CMSAlgorithm.SHA384, "sha-384");
        stdMicAlgs.put(CMSAlgorithm.SHA512, "sha-512");
        stdMicAlgs.put(CMSAlgorithm.GOST3411, "gostr3411-94");
        stdMicAlgs.put(CMSAlgorithm.GOST3411_2012_256, "gostr3411-2012-256");
        stdMicAlgs.put(CMSAlgorithm.GOST3411_2012_512, "gostr3411-2012-512");

        RFC5751_MICALGS = Collections.unmodifiableMap(stdMicAlgs);

        Map oldMicAlgs = new HashMap();

        oldMicAlgs.put(CMSAlgorithm.MD5, "md5");
        oldMicAlgs.put(CMSAlgorithm.SHA1, "sha1");
        oldMicAlgs.put(CMSAlgorithm.SHA224, "sha224");
        oldMicAlgs.put(CMSAlgorithm.SHA256, "sha256");
        oldMicAlgs.put(CMSAlgorithm.SHA384, "sha384");
        oldMicAlgs.put(CMSAlgorithm.SHA512, "sha512");
        oldMicAlgs.put(CMSAlgorithm.GOST3411, "gostr3411-94");
        oldMicAlgs.put(CMSAlgorithm.GOST3411_2012_256, "gostr3411-2012-256");
        oldMicAlgs.put(CMSAlgorithm.GOST3411_2012_512, "gostr3411-2012-512");


        RFC3851_MICALGS = Collections.unmodifiableMap(oldMicAlgs);

        STANDARD_MICALGS = RFC5751_MICALGS;


        Map<String, ASN1ObjectIdentifier> mic = new TreeMap<String, ASN1ObjectIdentifier>(String.CASE_INSENSITIVE_ORDER);

        for (Object key : STANDARD_MICALGS.keySet())
        {
            mic.put(STANDARD_MICALGS.get(key).toString(), (ASN1ObjectIdentifier)key);
        }

        for (Object key : RFC3851_MICALGS.keySet())
        {
            mic.put(RFC3851_MICALGS.get(key).toString(), (ASN1ObjectIdentifier)key);
        }

        forMic = Collections.unmodifiableMap(mic);

    }

    static String lessQuotes(String in)
    {
        if (in == null || in.length() <= 1)  // make sure we have at least 2 characters
        {
            return in;
        }

        if (in.charAt(0) == '"' && in.charAt(in.length() - 1) == '"')
        {
            return in.substring(1, in.length() - 1);
        }

        return in;
    }

    static String getParameter(String startsWith, List<String> parameters)
    {
        for (String param : parameters)
        {
            if (param.startsWith(startsWith))
            {
                return param;
            }
        }

        return null;
    }

    static ASN1ObjectIdentifier getDigestOID(String alg)
    {
        ASN1ObjectIdentifier oid = (ASN1ObjectIdentifier)forMic.get(Strings.toLowerCase(alg));

        if (oid == null)
        {
            throw new IllegalArgumentException("unknown micalg passed: " + alg);
        }

        return oid;
    }
}
