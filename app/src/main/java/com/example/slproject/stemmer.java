package com.example.slproject;

import java.util.List;

import kr.co.shineware.nlp.komoran.constant.DEFAULT_MODEL;
import kr.co.shineware.nlp.komoran.core.Komoran;
import kr.co.shineware.nlp.komoran.model.KomoranResult;
import kr.co.shineware.nlp.komoran.model.Token;

public class stemmer {
    public static List getStem(String text){
        Komoran komoran = new Komoran(DEFAULT_MODEL.FULL);
        KomoranResult analyzeResultList = komoran.analyze(text);

        List<Token> tokenList = analyzeResultList.getTokenList();
        for(Token token : tokenList){
            System.out.format("%s %s\n", token.getMorph(), token.getPos());
        }
        return tokenList;
    }
}
