/*
 * 题目：区间翻转 (Interval Reversal)
 * 来源：POJ 3580
 * 网址：http://poj.org/problem?id=3580
 * 
 * 问题描述：
 * 维护一个序列，支持以下操作：
 * 1. ADD x y D: 将区间[x,y]中的每个元素加上D
 * 2. REVERSE x y: 将区间[x,y]翻转
 * 3. REVOLVE x y T: 将区间[x,y]循环右移T次
 * 4. INSERT x P: 在位置x后插入P
 * 5. DELETE x: 删除位置x的元素
 * 6. MIN x y: 查询区间[x,y]的最小值
 * 
 * 时间复杂度：每个操作平均O(log n)
 * 空间复杂度：O(n)
 * 
 * 解题思路：
 * 使用Splay树维护序列，每个节点存储子树大小、最小值、懒标记
 * 通过splay操作实现高效的区间操作
 */

import java.io.*;
import java.util.*;

public class Code11_IntervalReversal {
    
    static class SplayNode {
        int key;           // 节点值
        int size;          // 子树大小
        int minVal;        // 子树最小值
        int addLazy;      // 加法懒标记
        boolean revLazy;  // 翻转懒标记
        SplayNode left;    // 左子树
        SplayNode right;   // 右子树
        SplayNode parent;  // 父节点
        
        SplayNode(int key) {
            this.key = key;
            this.size = 1;
            this.minVal = key;
            this.addLazy = 0;
            this.revLazy = false;
        }
    }
    
    static SplayNode root;
    static final int INF = 0x3f3f3f3f;
    
    // 维护子树信息
    static void maintain(SplayNode x) {
        if (x != null) {
            x.size = 1;
            x.minVal = x.key;
            
            if (x.left != null) {
                x.size += x.left.size;
                x.minVal = Math.min(x.minVal, x.left.minVal);
            }
            if (x.right != null) {
                x.size += x.right.size;
                x.minVal = Math.min(x.minVal, x.right.minVal);
            }
        }
    }
    
    // 下传懒标记
    static void pushDown(SplayNode x) {
        if (x != null) {
            if (x.addLazy != 0) {
                x.key += x.addLazy;
                if (x.left != null) {
                    x.left.addLazy += x.addLazy;
                    x.left.minVal += x.addLazy;
                }
                if (x.right != null) {
                    x.right.addLazy += x.addLazy;
                    x.right.minVal += x.addLazy;
                }
                x.addLazy = 0;
            }
            
            if (x.revLazy) {
                SplayNode temp = x.left;
                x.left = x.right;
                x.right = temp;
                if (x.left != null) x.left.revLazy = !x.left.revLazy;
                if (x.right != null) x.right.revLazy = !x.right.revLazy;
                x.revLazy = false;
            }
        }
    }
    
    // 左旋操作
    static void leftRotate(SplayNode x) {
        SplayNode y = x.right;
        pushDown(x);
        pushDown(y);
        
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
        pushDown(x);
        pushDown(y);
        
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
    
    // Splay操作
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
    
    // 获取第k小的节点
    static SplayNode getKth(int k) {
        if (root == null || k <= 0 || k > root.size) {
            return null;
        }
        
        SplayNode current = root;
        while (current != null) {
            pushDown(current);
            int leftSize = (current.left != null) ? current.left.size : 0;
            
            if (k == leftSize + 1) {
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
    
    // 分割区间[l, r]
    static SplayNode[] split(int l, int r) {
        if (l > 1) {
            SplayNode leftPart = getKth(l - 1);
            splay(leftPart);
            SplayNode rightPart = leftPart.right;
            leftPart.right = null;
            if (rightPart != null) rightPart.parent = null;
            maintain(leftPart);
            
            if (rightPart != null) {
                SplayNode midPart = getKth(r - l + 1);
                splay(midPart);
                SplayNode remaining = midPart.right;
                midPart.right = null;
                if (remaining != null) remaining.parent = null;
                maintain(midPart);
                
                return new SplayNode[]{leftPart, midPart, remaining};
            }
        } else {
            SplayNode midPart = getKth(r);
            splay(midPart);
            SplayNode remaining = midPart.right;
            midPart.right = null;
            if (remaining != null) remaining.parent = null;
            maintain(midPart);
            
            return new SplayNode[]{null, midPart, remaining};
        }
        return null;
    }
    
    // 合并子树
    static SplayNode merge(SplayNode left, SplayNode right) {
        if (left == null) return right;
        if (right == null) return left;
        
        SplayNode maxNode = left;
        while (maxNode.right != null) {
            maxNode = maxNode.right;
        }
        splay(maxNode);
        maxNode.right = right;
        right.parent = maxNode;
        maintain(maxNode);
        
        return maxNode;
    }
    
    // 区间加法
    static void addInterval(int l, int r, int d) {
        SplayNode[] parts = split(l, r);
        if (parts[1] != null) {
            parts[1].addLazy += d;
            parts[1].minVal += d;
        }
        root = merge(merge(parts[0], parts[1]), parts[2]);
    }
    
    // 区间翻转
    static void reverseInterval(int l, int r) {
        SplayNode[] parts = split(l, r);
        if (parts[1] != null) {
            parts[1].revLazy = !parts[1].revLazy;
        }
        root = merge(merge(parts[0], parts[1]), parts[2]);
    }
    
    // 区间循环右移
    static void revolveInterval(int l, int r, int t) {
        int len = r - l + 1;
        t %= len;
        if (t == 0) return;
        
        SplayNode[] parts = split(l, r);
        if (parts[1] != null) {
            SplayNode[] subParts = split(1, len - t);
            parts[1] = merge(subParts[1], subParts[0]);
        }
        root = merge(merge(parts[0], parts[1]), parts[2]);
    }
    
    // 插入节点
    static void insert(int pos, int val) {
        SplayNode newNode = new SplayNode(val);
        if (pos == 0) {
            if (root == null) {
                root = newNode;
            } else {
                SplayNode minNode = root;
                while (minNode.left != null) {
                    minNode = minNode.left;
                }
                splay(minNode);
                minNode.left = newNode;
                newNode.parent = minNode;
                maintain(minNode);
            }
        } else {
            SplayNode node = getKth(pos);
            splay(node);
            newNode.right = node.right;
            if (node.right != null) node.right.parent = newNode;
            node.right = newNode;
            newNode.parent = node;
            maintain(newNode);
            maintain(node);
        }
    }
    
    // 删除节点
    static void delete(int pos) {
        SplayNode node = getKth(pos);
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
    
    // 查询区间最小值
    static int queryMin(int l, int r) {
        SplayNode[] parts = split(l, r);
        int minVal = (parts[1] != null) ? parts[1].minVal : INF;
        root = merge(merge(parts[0], parts[1]), parts[2]);
        return minVal;
    }
    
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter out = new PrintWriter(System.out);
        
        int n = Integer.parseInt(br.readLine());
        String[] arr = br.readLine().split(" ");
        
        // 初始化序列
        root = null;
        for (int i = 0; i < n; i++) {
            insert(i, Integer.parseInt(arr[i]));
        }
        
        int m = Integer.parseInt(br.readLine());
        for (int i = 0; i < m; i++) {
            String[] command = br.readLine().split(" ");
            String op = command[0];
            
            switch (op) {
                case "ADD":
                    int x1 = Integer.parseInt(command[1]);
                    int y1 = Integer.parseInt(command[2]);
                    int d = Integer.parseInt(command[3]);
                    addInterval(x1, y1, d);
                    break;
                case "REVERSE":
                    int x2 = Integer.parseInt(command[1]);
                    int y2 = Integer.parseInt(command[2]);
                    reverseInterval(x2, y2);
                    break;
                case "REVOLVE":
                    int x3 = Integer.parseInt(command[1]);
                    int y3 = Integer.parseInt(command[2]);
                    int t = Integer.parseInt(command[3]);
                    revolveInterval(x3, y3, t);
                    break;
                case "INSERT":
                    int pos = Integer.parseInt(command[1]);
                    int val = Integer.parseInt(command[2]);
                    insert(pos, val);
                    break;
                case "DELETE":
                    int delPos = Integer.parseInt(command[1]);
                    delete(delPos);
                    break;
                case "MIN":
                    int x4 = Integer.parseInt(command[1]);
                    int y4 = Integer.parseInt(command[2]);
                    out.println(queryMin(x4, y4));
                    break;
            }
        }
        
        out.flush();
        out.close();
    }
}