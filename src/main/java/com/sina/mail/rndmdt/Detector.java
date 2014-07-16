package com.sina.mail.rndmdt;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.google.common.base.Charsets;
import com.google.common.base.Splitter;
import com.google.common.io.Resources;

/**
 * @author limeng
 *	memoMap is used to store the intermediate results
 */
public class Detector {

    private Probability probability;
    private HashMap<String, String> memoMap;
    
    public Detector(){
        memoMap=new HashMap<String, String>(20);
    }

    /**
     * @param path
     * get the word probability distribution via file with each line like "${world}	${count}"
     */
    public void computeProbDist(String path){
    	try {
			probability = new Probability(path);
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    /**
     * @throws IOException
     * get the word probability distribution via the word_count.txt in ./resource/"
     */
    public void computeDefaultProbDist() throws IOException{
    	URL url = Resources.getResource("word_count.txt");
    	List<String> lines = Resources.readLines(url, Charsets.UTF_8);
    	this.probability=new Probability(lines);
    	
    }
    
    /**
     * @param text
     * @return
     * get a word's segments
     */
    public String segment(String text){
    	this.memoMap.clear();
    	return recursion(text);
    }
    
    public String recursion(String text){
    	
    	if (memoMap.containsKey(text))
    		return memoMap.get(text);
    	
        Iterator<String> iter = this.splits(text).iterator();
        List<String> cand=new LinkedList<String>();
        while (iter.hasNext()){
            String s = iter.next();
            List<String> splits = Splitter.on(',').omitEmptyStrings().trimResults().splitToList(s);
            
            if (splits.size()==2){
                String ss=splits.get(0)+","+recursion(splits.get(1));
                cand.add(ss);
            }
            else{
                cand.add(splits.get(0));
            }
        }
        String maxCand="";
        double maxProb=0.0;
        
        for (String c : cand) {
            double product=product(c);
            if (product>maxProb){
                maxProb=product;
                maxCand=c;
            }
        }
        memoMap.put(text, maxCand);
        return maxCand;
    }
    
    //split a word into [first,rem]
    public List<String> splits(String text){
        List<String> list=new LinkedList<String>();
        if (text.length()<4){
            list.add(text);
            return list;
        }
        for (int i=Math.min(1,text.length()); i<Math.min(text.length(), 10);i++){
            String s=text.substring(0, i)+','+text.substring(i, text.length());
            list.add(s);
        }
        return list;
    }
    
    /**
     * @param text
     * @return
     * count a segments' probability
     */
    public double product(String text){
        Iterable<String> splits = Splitter.on(',').split(text);
        double prob=1;
        for (String s : splits) {
            prob*=this.probability.getProb(s);
        }
        return prob;
    }
}
