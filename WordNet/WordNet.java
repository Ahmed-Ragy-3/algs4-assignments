import java.util.ArrayList;
import java.util.HashMap;

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;

public class WordNet {
   // specific --> general

   private SAP sapFinder;
   private String[] synsets;
   private HashMap<String, ArrayList<Integer>> nounsMap;
   
   // constructor takes the name of the two input files
   public WordNet(String synsets, String hypernyms) {
      check(synsets, hypernyms);
      // synsets = nodes, hypernyms = edges
      // hyponym (more specific synset), hypernym (more general synset)
      String[] synsetsLines = new In(synsets).readAllLines();
      String[] hypernymsLines = new In(hypernyms).readAllLines();
      
      int numOfLines = synsetsLines.length;
      Digraph wordnet = new Digraph(numOfLines);
      this.synsets = new String[numOfLines];
      this.nounsMap = new HashMap<>(numOfLines);
      
      for (int i = 0; i < numOfLines; i++) {
         String[] synsetsLine = synsetsLines[i].split(",");
         String[] hypernymsLine = hypernymsLines[i].split(",");
         
         int id = Integer.parseInt(synsetsLine[0]);
         String synset = synsetsLine[1];
         this.synsets[id] = synset;
         
         // extract nouns of synset
         String[] nouns = synset.split(" ");
         for (String noun : nouns) {
            if (!nounsMap.containsKey(noun)) {
               nounsMap.put(noun, new ArrayList<>());
            }
            nounsMap.get(noun).add(id);
         }
         
         // add relations
         for (int j = 1; j < hypernymsLine.length; j++) {
            int toId = Integer.parseInt(hypernymsLine[j]);
            wordnet.addEdge(id, toId);
         }
      }
      
      sapFinder = new SAP(wordnet);
   }

   private void check(Object... args) {
      for (Object arg : args) {
         if (arg == null)
            throw new IllegalArgumentException();
      }
   }
   
   // returns all WordNet nouns
   public Iterable<String> nouns() {
      return nounsMap.keySet();
   }
   
   // is the word a WordNet noun?
   public boolean isNoun(String word) {
      return nounsMap.containsKey(word);
   }

   // distance between nounA and nounB (defined below)
   public int distance(String nounA, String nounB) {
      check(nounA, nounB);
      if (!isNoun(nounA) || !isNoun(nounB))
         throw new IllegalArgumentException();

      Iterable<Integer> u = nounsMap.get(nounA);
      Iterable<Integer> v = nounsMap.get(nounB);

      return sapFinder.length(u, v);
   }

   // a synset (second field of synsets.txt) that is the common ancestor of nounA
   // and nounB
   // in a shortest ancestral path (defined below)
   public String sap(String nounA, String nounB) {
      check(nounA, nounB);
      if (!isNoun(nounA) || !isNoun(nounB))
         throw new IllegalArgumentException();

      Iterable<Integer> u = nounsMap.get(nounA);
      Iterable<Integer> v = nounsMap.get(nounB);

      int synsetId = sapFinder.ancestor(u, v);
      return synsets[synsetId];
   }

   // do unit testing of this class
   public static void main(String[] args) {
      WordNet wn = new WordNet(args[0], args[1]);
      for (String str : wn.nouns()) {
         System.out.println(str);
      }
   }
}