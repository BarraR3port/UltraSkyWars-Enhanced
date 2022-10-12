/*
 * Copyright (c) 2022.
 *
 *  This program/library is free software: you can redistribute it and/or modify
 * it under the terms of the New BSD License (3-clause license).
 *
 * This program/library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the New BSD License (3-clause license)
 * for more details.
 *
 * You should have received a copy of the New BSD License (3-clause license)
 * along with this program/library; If not, see http://directory.fsf.org/wiki/License:BSD_3Clause/
 * for the New BSD License (3-clause license).
 *
 */

package io.github.Leonardo0013YT.UltraSkyWars.api.utils;

import java.util.HashMap;

public class CenterMessage {
    
    public static HashMap<String, String> cached = new HashMap<>();
    
    public static String getCenteredMessage(String string){
        if (cached.containsKey(string)){
            return cached.get(string);
        }
        Object object;
        int n = 0;
        boolean bl = false;
        boolean bl2 = false;
        for ( int n2 : string.toCharArray() ){
            if (n2 == 167){
                bl = true;
                continue;
            }
            if (bl){
                bl = false;
                if (n2 == 108 || n2 == 76){
                    bl2 = true;
                    continue;
                }
                bl2 = false;
                continue;
            }
            object = DefaultFontInfo.getDefaultFontInfo((char) n2);
            n += bl2 ? ((DefaultFontInfo) object).getBoldLength() : ((DefaultFontInfo) object).getLength();
            ++n;
        }
        int n3 = n / 2;
        int n4 = 154 - n3;
        int n5 = DefaultFontInfo.SPACE.getLength() + 1;
        object = new StringBuilder();
        for ( int n2 = 0; n2 < n4; n2 += n5 ){
            ((StringBuilder) object).append(" ");
        }
        String centered = object + string;
        cached.put(string, centered);
        return centered;
    }
    
    public enum DefaultFontInfo {
        A('A', 5),
        a('a', 5),
        B('B', 5),
        b('b', 5),
        C('C', 5),
        c('c', 5),
        D('D', 5),
        d('d', 5),
        E('E', 5),
        e('e', 5),
        F('F', 5),
        f('f', 4),
        G('G', 5),
        g('g', 5),
        H('H', 5),
        h('h', 5),
        I('I', 3),
        i('i', 1),
        J('J', 5),
        j('j', 5),
        K('K', 5),
        k('k', 4),
        L('L', 5),
        l('l', 1),
        M('M', 5),
        m('m', 5),
        N('N', 5),
        n('n', 5),
        O('O', 5),
        o('o', 5),
        P('P', 5),
        p('p', 5),
        Q('Q', 5),
        q('q', 5),
        R('R', 5),
        r('r', 5),
        S('S', 5),
        s('s', 5),
        T('T', 5),
        t('t', 4),
        U('U', 5),
        u('u', 5),
        V('V', 5),
        v('v', 5),
        W('W', 5),
        w('w', 5),
        X('X', 5),
        x('x', 5),
        Y('Y', 5),
        y('y', 5),
        Z('Z', 5),
        z('z', 5),
        NUM_1('1', 5),
        NUM_2('2', 5),
        NUM_3('3', 5),
        NUM_4('4', 5),
        NUM_5('5', 5),
        NUM_6('6', 5),
        NUM_7('7', 5),
        NUM_8('8', 5),
        NUM_9('9', 5),
        NUM_0('0', 5),
        EXCLAMATION_POINT('!', 1),
        AT_SYMBOL('@', 6),
        NUM_SIGN('#', 5),
        DOLLAR_SIGN('$', 5),
        PERCENT('%', 5),
        UP_ARROW('^', 5),
        AMPERSAND('&', 5),
        ASTERISK('*', 5),
        LEFT_PARENTHESIS('(', 4),
        RIGHT_PERENTHESIS(')', 4),
        MINUS('-', 5),
        UNDERSCORE('_', 5),
        PLUS_SIGN('+', 5),
        EQUALS_SIGN('=', 5),
        LEFT_CURL_BRACE('{', 4),
        RIGHT_CURL_BRACE('}', 4),
        LEFT_BRACKET('[', 3),
        RIGHT_BRACKET(']', 3),
        COLON(':', 1),
        SEMI_COLON(';', 1),
        DOUBLE_QUOTE('\"', 3),
        SINGLE_QUOTE('\'', 1),
        LEFT_ARROW('<', 4),
        RIGHT_ARROW('>', 4),
        QUESTION_MARK('?', 5),
        SLASH('/', 5),
        BACK_SLASH('\\', 5),
        LINE('|', 1),
        TILDE('~', 5),
        TICK('`', 2),
        PERIOD('.', 1),
        COMMA(',', 1),
        SPACE(' ', 3),
        DEFAULT('a', 4);
        
        private final char character;
        private final int length;
        
        DefaultFontInfo(char c, int n2){
            this.character = c;
            this.length = n2;
        }
        
        public static DefaultFontInfo getDefaultFontInfo(char c){
            for ( DefaultFontInfo defaultFontInfo : DefaultFontInfo.values() ){
                if (defaultFontInfo.getCharacter() != c) continue;
                return defaultFontInfo;
            }
            return DEFAULT;
        }
        
        public char getCharacter(){
            return this.character;
        }
        
        public int getLength(){
            return this.length;
        }
        
        public int getBoldLength(){
            if (this == SPACE){
                return this.getLength();
            }
            return this.length + 1;
        }
    }
}
