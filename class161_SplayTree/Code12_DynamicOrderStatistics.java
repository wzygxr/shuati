/*
 * 题目：动态顺序统计 (Dynamic Order Statistics)
 * 来源：SPOJ ORDERSET
 * 网址：https://www.spoj.com/problems/ORDERSET/
 * 
 * 问题描述：
 * 维护一个动态集合，支持以下操作：
 * 1. I x: 插入元素x（如果x不存在）
 * 2. D x: 删除元素x（如果x存在）
 * 3. K x: 查询第x小的元素
 * 4. C x: 查询小于x的元素个数
 * 
 * 时间复杂度：每个操作平均O(log n)
 * 空间复杂度：O(n)
 * 
 * 解题思路：
 * 使用Splay树维护动态集合，每个节点存储子树大小
 * 通过splay操作实现高效的插入、删除、查询操作
 */

import java.io.*;
import java.util.*;

public class Code12_DynamicOrderStatistics {
    
    static class SplayNode {
        int key;           // 节点值
        int size;          // 子树大小
        SplayNode left;    // 左子树
        SplayNode right;   // 右子树
        SplayNode parent;  // 父节点
        
        SplayNode(int key) {
            this.key = key;
            this.size = 1;
        }
    }
    
    static SplayNode root;
    
    // 维护子树大小
    static void maintain(SplayNode x) {
        if (x != null) {
            x.size = 1;
            if (x.left != null) x.size += x.left.size;
            if (x.right != null) x.size += x.right.size;
        }
    }
    
    // 左旋操作
    static void leftRotate(SplayNode x) {
        SplayNode y = x.right;
        if (y != null) {
            x.right = y.left;
            if (y.left != null) y.left.parent = x;
            y.parent = x.parent;
        }
        
        if (x.parent == null) {
            root = y;
        } else if (x == x.parent.left) {
            x.parent.left = y;
        } else {
            x.parent.right = y;
        }
        
        if (y != null) y.left = x;
        x.parent = y;
        
        maintain(x);
        maintain(y);
    }
    
    // 右旋操作
    static void rightRotate(SplayNode x) {
        SplayNode y = x.left;
        if (y != null) {
            x.left = y.right;
            if (y.right != null) y.right.parent = x;
            y.parent = x.parent;
        }
        
        if (x.parent == null) {
            root = y;
        } else if (x == x.parent.left) {
            x.parent.left = y;
        } else {
            x.parent.right = y;
        }
        
        if (y != null) y.right = x;
        x.parent = y;
        
        maintain(x);
        maintain(y);
    }
    
    // Splay操作：将节点x旋转到根
    static void splay(SplayNode x) {
        while (x.parent != null) {
            if (x.parent.parent == null) {
                if (x == x.parent.left) {
                    rightRotate(x.parent);
                } else {
                    leftRotate(x.parent);
                }
            } else {
                SplayNode parent = x.parent;
                SplayNode grandParent = parent.parent;
                
                if (parent.left == x && grandParent.left == parent) {
                    rightRotate(grandParent);
                    rightRotate(parent);
                } else if (parent.right == x && grandParent.right == parent) {
                    leftRotate(grandParent);
                    leftRotate(parent);
                } else if (parent.left == x && grandParent.right == parent) {
                    rightRotate(parent);
                    leftRotate(grandParent);
                } else {
                    leftRotate(parent);
                    rightRotate(grandParent);
                }
            }
        }
    }
    
    // 插入节点
    static void insert(int key) {
        if (root == null) {
            root = new SplayNode(key);
            return;
        }
        
        SplayNode current = root;
        SplayNode parent = null;
        
        while (current != null) {
            parent = current;
            if (key == current.key) {
                // 元素已存在，不需要重复插入
                splay(current);
                return;
            } else if (key < current.key) {
                current = current.left;
            } else {
                current = current.right;
            }
        }
        
        SplayNode newNode = new SplayNode(key);
        if (key < parent.key) {
            parent.left = newNode;
        } else {
            parent.right = newNode;
        }
        newNode.parent = parent;
        
        splay(newNode);
    }
    
    // 查找节点
    static SplayNode find(int key) {
        SplayNode current = root;
        while (current != null) {
            if (key == current.key) {
                splay(current);
                return current;
            } else if (key < current.key) {
                current = current.left;
            } else {
                current = current.right;
            }
        }
        return null;
    }
    
    // 删除节点
    static void delete(int key) {
        SplayNode node = find(key);
        if (node == null) return; // 元素不存在
        
        splay(node);
        
        if (node.left == null) {
            root = node.right;
            if (root != null) root.parent = null;
        } else if (node.right == null) {
            root = node.left;
            if (root != null) root.parent = null;
        } else {
            SplayNode leftTree = node.left;
            leftTree.parent = null;
            SplayNode rightTree = node.right;
            rightTree.parent = null;
            
            // 找到左子树的最大节点
            SplayNode maxNode = leftTree;
            while (maxNode.right != null) {
                maxNode = maxNode.right;
            }
            splay(maxNode);
            
            maxNode.right = rightTree;
            rightTree.parent = maxNode;
            maintain(maxNode);
            root = maxNode;
        }
    }
    
    // 获取第k小的元素
    static SplayNode getKth(int k) {
        if (root == null || k <= 0 || k > root.size) {
            return null;
        }
        
        SplayNode current = root;
        while (current != null) {
            int leftSize = (current.left != null) ? current.left.size : 0;
            
            if (k == leftSize + 1) {
                splay(current);
                return current;
            } else if (k <= leftSize) {
                current = current.left;
            } else {
                k -= leftSize + 1;
                current = current.right;
            }
        }
        return null;
    }
    
    // 查询小于x的元素个数
    static int countLessThan(int x) {
        if (root == null) return 0;
        
        SplayNode current = root;
        int count = 0;
        
        while (current != null) {
            if (x > current.key) {
                count += 1 + ((current.left != null) ? current.left.size : 0);
                current = current.right;
            } else {
                current = current.left;
            }
        }
        
        return count;
    }
    
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter out = new PrintWriter(System.out);
        
        int Q = Integer.parseInt(br.readLine());
        
        for (int i = 0; i < Q; i++) {
            String[] command = br.readLine().split(" ");
            char op = command[0].charAt(0);
            int x = Integer.parseInt(command[1]);
            
            switch (op) {
                case 'I':
                    insert(x);
                    break;
                case 'D':
                    delete(x);
                    break;
                case 'K':
                    SplayNode kthNode = getKth(x);
                    if (kthNode != null) {
                        out.println(kthNode.key);
                    } else {
                        out.println("invalid");
                    }
                    break;
                case 'C':
                    out.println(countLessThan(x));
                    break;
            }
        }
        
        out.flush();
        out.close();
    }
}