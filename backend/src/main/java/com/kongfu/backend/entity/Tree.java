package com.kongfu.backend.entity;

import java.util.List;

/**
 * 树结构实体
 */
public class Tree {

    private String text;
    private List<Tree> nodes;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<Tree> getNodes() {
        return nodes;
    }

    public void setNodes(List<Tree> nodes) {
        this.nodes = nodes;
    }

    public Tree(String text,  List<Tree> nodes) {
        this.text = text;
        this.nodes = nodes;
    }
    public Tree(String text) {
        this.text = text;
    }
    public Tree() {
    }
}
