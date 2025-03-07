package org.bluebird.Extractor;

import org.antlr.v4.runtime.misc.MultiMap;
import org.antlr.v4.runtime.misc.OrderedHashSet;
import org.bluebird.FileUtils.FileCreator;

import java.util.List;
import java.util.Set;

public class CallGraph {

    private static Set<String> nodes = new OrderedHashSet<>();
    private static MultiMap<String, String> edges = new MultiMap<>();
    private static Set<String> notCalledMth = new OrderedHashSet<>();

    /**
     * Adiciona aresta ligando duas entidades no grafo
     * @param source Vertice fonte
     * @param target Vertice alvo
     */
    public void setEdge(String source, String target) {
        edges.map(source, target);
    }

    /**
     * Adiciona entidade como vertice no grafo
     * @param node Vertice
     */
    public void setNodes(String node) {
        nodes.add(node);
    }

    /**
     * Procura metodos nao chamados
     */
    private static void searchNotCalledMth() {
        Set<String> calledMth = new OrderedHashSet<>();

        for (String node : CallGraph.nodes) {
            for (List<String> src : edges.values()) {
                if (src.contains(node)) {
                    calledMth.add(node);
                }
            }
        }

        for(String node : CallGraph.nodes) {
            if (!calledMth.contains(node) && !(node.toLowerCase().equals("main"))) {
                CallGraph.notCalledMth.add(node);
            }
        }
    }

    /**
     * Converte a string do grafo para o arquivo dot
     */
    public static void toDOT() {
        FileCreator.appendToDotFile("digraph G{\n");

        for (String node : nodes) {
            FileCreator.appendToDotFile(node);
            FileCreator.appendToDotFile("; ");
        }

        FileCreator.appendToDotFile("\n");

        for (String src : edges.keySet()) {
            for (String trg : edges.get(src)) {
                FileCreator.appendToDotFile(" ");
                FileCreator.appendToDotFile("\t" + src);
                FileCreator.appendToDotFile("  ->  ");
                FileCreator.appendToDotFile(trg);
                FileCreator.appendToDotFile(";\n");
            }
        }

        FileCreator.appendToDotFile("}\n");
        nodes.clear();
        edges.clear();
    }

    /**
     * Converte a analise feita do grafo para o txt
     */
    public static void toTxt() {
        CallGraph.searchNotCalledMth();

        FileCreator.appendToTxtFile("Métodos ou Propertys não chamados:\n");

        for (String node : CallGraph.notCalledMth) {
            FileCreator.appendToTxtFile(node);
            FileCreator.appendToTxtFile("\n");
        }

        notCalledMth.clear();
    }
}
