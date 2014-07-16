package com.sina.mail.rndmdt;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import com.google.common.base.Splitter;
import com.google.common.io.Files;

/**
 * @author limeng
 *
 */
public class Probability {

    private HashMap<String, Double> map;

    
    /**
     * @param lines
     */
    public Probability(List<String> lines) {
        this.map=new HashMap<String, Double>(300000);
        long sum=0l;
        for (String l : lines) {
            List<String> splits = Splitter.on('\t').trimResults().omitEmptyStrings().splitToList(l);
            sum+=Long.parseLong(splits.get(1));
        }
        for (String l : lines) {
            List<String> splits = Splitter.on('\t').trimResults().omitEmptyStrings().splitToList(l);
            double rate=Double.parseDouble(splits.get(1))/sum;
            String word=splits.get(0);
            this.map.put(word, rate);
        }

	}

    /**
     * @param lines
     * build probability via file
     */
    public Probability(String path) throws IOException {
        this.map=new HashMap<String, Double>(30000);
        List<String> lines = Files.readLines(new File(path), Charset.defaultCharset());
        long sum=0l;
        for (String l : lines) {
            List<String> splits = Splitter.on('\t').trimResults().omitEmptyStrings().splitToList(l);
            sum+=Long.parseLong(splits.get(1));
        }
        for (String l : lines) {
            List<String> splits = Splitter.on('\t').trimResults().omitEmptyStrings().splitToList(l);
            double rate=Double.parseDouble(splits.get(1))/sum;
            String word=splits.get(0);
            this.map.put(word, rate);
        }
    }
    
    public double getProb(String word){
        if (word==null || word.length()==0)
//            return Math.pow(10, -5);
            return 0.0;
        if (!this.map.containsKey(word)){
            return 0.0;
//            return Math.pow(10, -5);
        }
        return this.map.get(word);
    }
}
