/*
 * 题目：序列操作 (Sequence Operations)
 * 来源：HDU 3436
 * 网址：http://acm.hdu.edu.cn/showproblem.php?pid=3436
 * 
 * 问题描述：
 * 维护一个序列，支持以下操作：
 * 1. TOP x: 将元素x移动到序列开头
 * 2. QUERY x: 查询元素x在序列中的位置
 * 3. RANK x: 查询序列中第x个位置的元素
 * 
 * 时间复杂度：每个操作平均O(log n)
 * 空间复杂度：O(n)
 * 
 * 解题思路：
 * 使用Splay树维护序列，每个节点存储子树大小用于快速定位
 * 通过splay操作实现高效的区间操作和位置查询
 */

import java.io.*;
import java.util.*;

public class Code10_SequenceOperations {
    
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
    static Map<Integer, SplayNode> nodeMap = new HashMap<>();
    
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
                // 父节点是根节点
                if (x == x.parent.left) {
                    rightRotate(x.parent);
                } else {
                    leftRotate(x.parent);
                }
            } else {
                SplayNode parent = x.parent;
                SplayNode grandParent = parent.parent;
                
                if (parent.left == x && grandParent.left == parent) {
                    // LL情况
                    rightRotate(grandParent);
                    rightRotate(parent);
                } else if (parent.right == x && grandParent.right == parent) {
                    // RR情况
                    leftRotate(grandParent);
                    leftRotate(parent);
                } else if (parent.left == x && grandParent.right == parent) {
                    // LR情况
                    rightRotate(parent);
                    leftRotate(grandParent);
                } else {
                    // RL情况
                    leftRotate(parent);
                    rightRotate(grandParent);
                }
            }
        }
    }
    
    // 插入节点
    static void insert(int key) {
        SplayNode newNode = new SplayNode(key);
        nodeMap.put(key, newNode);
        
        if (root == null) {
            root = newNode;
            return;
        }
        
        SplayNode current = root;
        SplayNode parent = null;
        
        while (current != null) {
            parent = current;
            if (key < current.key) {
                current = current.left;
            } else {
                current = current.right;
            }
        }
        
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
    
    // 获取节点的排名
    static int getRank(SplayNode x) {
        if (x == null) return -1;
        splay(x);
        return (x.left != null) ? x.left.size + 1 : 1;
    }
    
    // 将节点移动到开头
    static void moveToFront(int key) {
        SplayNode node = find(key);
        if (node == null) return;
        
        // 如果已经是第一个节点，不需要移动
        if (node.left == null) return;
        
        // 分离左子树
        SplayNode leftTree = node.left;
        node.left = null;
        leftTree.parent = null;
        maintain(node);
        
        // 找到左子树的最大节点
        SplayNode maxNode = leftTree;
        while (maxNode.right != null) {
            maxNode = maxNode.right;
        }
        splay(maxNode);
        
        // 将原节点插入到左子树最大节点的右侧
        maxNode.right = node;
        node.parent = maxNode;
        maintain(maxNode);
        
        root = maxNode;
    }
    
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter out = new PrintWriter(System.out);
        
        int T = Integer.parseInt(br.readLine());
        for (int t = 1; t <= T; t++) {
            String[] nm = br.readLine().split(" ");
            int n = Integer.parseInt(nm[0]);
            int m = Integer.parseInt(nm[1]);
            
            // 初始化Splay树
            root = null;
            nodeMap.clear();
            
            // 插入初始序列
            for (int i = 1; i <= n; i++) {
                insert(i);
            }
            
            out.println("Case " + t + ":");
            
            for (int i = 0; i < m; i++) {
                String[] command = br.readLine().split(" ");
                String op = command[0];
                int x = Integer.parseInt(command[1]);
                
                switch (op) {
                    case "TOP":
                        moveToFront(x);
                        break;
                    case "QUERY":
                        SplayNode node = find(x);
                        if (node != null) {
                            out.println(getRank(node));
                        }
                        break;
                    case "RANK":
                        SplayNode kthNode = getKth(x);
                        if (kthNode != null) {
                            out.println(kthNode.key);
                        }
                        break;
                }
            }
        }
        
        out.flush();
        out.close();
    }
}