package org.elasticsearch.analysis;

public class TokenizerOptions {

    //한영오타에 대한 토큰 추출여부 (hello -> ㅗㄷㅣㅣㅐ)
    public final static boolean MISTYPE = false;

    //초성검색을 위한 토큰 추출여부 (가방 -> ㄱㅂ)
    public final static boolean CHOSUNG = false;

    private boolean mistype = MISTYPE;
    private boolean chosung = CHOSUNG;

    private String name = null;

    private TokenizerOptions(String name) {
        this.name = name;
    }

    public static TokenizerOptions create(String name) {
        return new TokenizerOptions(name);
    }

    public String getName() {
        return name;
    }

    public boolean isMistype() {
        return mistype;
    }

    public void setMistype(boolean mistype) {
        this.mistype = mistype;
    }

    public boolean isChosung() {
        return chosung;
    }

    public void setChosung(boolean chosung) {
        this.chosung = chosung;
    }
}
