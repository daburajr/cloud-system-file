package br.com.jr.cloudsystemfile.infra.util;

import com.github.f4b6a3.tsid.TsidCreator;

public class KeyGenerator {

    private KeyGenerator() {}
    public static long next() {
        return TsidCreator.getTsid().toLong();
    }
}
