/*
 * 题目：文本编辑器 (Text Editor)
 * 来源：UVa 11922
 * 网址：https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=3073
 * 
 * 问题描述：
 * 实现一个文本编辑器，支持以下操作：
 * 1. MOVE k: 将光标移动到第k个字符后
 * 2. INSERT n str: 在光标后插入长度为n的字符串str
 * 3. DELETE n: 删除光标后的n个字符
 * 4. GET n: 获取光标后的n个字符
 * 5. PREV: 光标前移一个位置
 * 6. NEXT: 光标后移一个位置
 * 
 * 时间复杂度：每个操作平均O(log n)
 * 空间复杂度：O(n)
 * 
 * 解题思路：
 * 使用Splay树维护文本序列，每个节点存储字符和子树大小
 * 通过splay操作实现高效的光标移动和文本编辑
 */

import java.io.*;
import java.util.*;

public class Code13_TextEditor {
    
    static class SplayNode {
        char ch;           // 字符
        int size;          // 子树大小
        SplayNode left;    // 左子树
        SplayNode right;   // 右子树
        SplayNode parent;  // 父节点
        
        SplayNode(char ch) {
            this.ch = ch;
            this.size = 1;
        }
    }
    
    static SplayNode root;
    static int cursorPos = 0; // 光标位置（在字符后）
    
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
    
    // 获取第k个字符节点
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
    
    // 在光标后插入字符串
    static void insert(String str) {
        if (str.isEmpty()) return;
        
        // 创建新节点的子树
        SplayNode newTree = buildTree(str.toCharArray(), 0, str.length() - 1);
        
        if (root == null) {
            root = newTree;
            cursorPos = str.length();
            return;
        }
        
        if (cursorPos == 0) {
            // 插入到开头
            SplayNode minNode = root;
            while (minNode.left != null) {
                minNode = minNode.left;
            }
            splay(minNode);
            minNode.left = newTree;
            newTree.parent = minNode;
            maintain(minNode);
        } else if (cursorPos == root.size) {
            // 插入到末尾
            SplayNode maxNode = root;
            while (maxNode.right != null) {
                maxNode = maxNode.right;
            }
            splay(maxNode);
            maxNode.right = newTree;
            newTree.parent = maxNode;
            maintain(maxNode);
        } else {
            // 插入到中间
            SplayNode node = getKth(cursorPos);
            splay(node);
            
            SplayNode rightTree = node.right;
            node.right = null;
            if (rightTree != null) rightTree.parent = null;
            maintain(node);
            
            // 连接新子树
            node.right = newTree;
            newTree.parent = node;
            maintain(node);
            
            // 连接右子树
            if (rightTree != null) {
                SplayNode maxNode = newTree;
                while (maxNode.right != null) {
                    maxNode = maxNode.right;
                }
                splay(maxNode);
                maxNode.right = rightTree;
                rightTree.parent = maxNode;
                maintain(maxNode);
            }
        }
        
        cursorPos += str.length();
    }
    
    // 构建平衡的Splay树
    static SplayNode buildTree(char[] chars, int start, int end) {
        if (start > end) return null;
        
        int mid = (start + end) / 2;
        SplayNode node = new SplayNode(chars[mid]);
        
        node.left = buildTree(chars, start, mid - 1);
        node.right = buildTree(chars, mid + 1, end);
        
        if (node.left != null) node.left.parent = node;
        if (node.right != null) node.right.parent = node;
        
        maintain(node);
        return node;
    }
    
    // 删除光标后的n个字符
    static void delete(int n) {
        if (root == null || n <= 0 || cursorPos + n > root.size) {
            return;
        }
        
        if (cursorPos == 0) {
            // 删除开头n个字符
            SplayNode node = getKth(n);
            splay(node);
            root = node.right;
            if (root != null) root.parent = null;
        } else if (cursorPos + n == root.size) {
            // 删除末尾n个字符
            SplayNode node = getKth(cursorPos);
            splay(node);
            node.right = null;
            maintain(node);
        } else {
            // 删除中间n个字符
            SplayNode leftNode = getKth(cursorPos);
            splay(leftNode);
            
            SplayNode rightNode = getKth(cursorPos + n + 1);
            splay(rightNode);
            
            // 分离要删除的部分
            SplayNode deletePart = leftNode.right;
            leftNode.right = null;
            maintain(leftNode);
            
            // 重新连接
            leftNode.right = rightNode;
            rightNode.parent = leftNode;
            maintain(leftNode);
        }
    }
    
    // 获取光标后的n个字符
    static String get(int n) {
        if (root == null || n <= 0 || cursorPos + n > root.size) {
            return "";
        }
        
        StringBuilder sb = new StringBuilder();
        getSubstring(cursorPos + 1, cursorPos + n, sb);
        return sb.toString();
    }
    
    // 中序遍历获取子字符串
    static void getSubstring(int start, int end, StringBuilder sb) {
        if (start > end) return;
        
        SplayNode node = getKth(start);
        splay(node);
        
        // 中序遍历获取字符
        inorder(node, sb, end - start + 1);
    }
    
    // 中序遍历
    static void inorder(SplayNode node, StringBuilder sb, int count) {
        if (node == null || count <= 0) return;
        
        inorder(node.left, sb, count);
        if (sb.length() < count) {
            sb.append(node.ch);
        }
        if (sb.length() < count) {
            inorder(node.right, sb, count - sb.length());
        }
    }
    
    // 光标前移
    static void prev() {
        if (cursorPos > 0) {
            cursorPos--;
        }
    }
    
    // 光标后移
    static void next() {
        if (root != null && cursorPos < root.size) {
            cursorPos++;
        }
    }
    
    // 移动光标
    static void move(int k) {
        if (root == null) {
            cursorPos = 0;
        } else {
            cursorPos = Math.max(0, Math.min(k, root.size));
        }
    }
    
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter out = new PrintWriter(System.out);
        
        int T = Integer.parseInt(br.readLine());
        
        for (int t = 0; t < T; t++) {
            root = null;
            cursorPos = 0;
            
            int Q = Integer.parseInt(br.readLine());
            
            for (int i = 0; i < Q; i++) {
                String[] command = br.readLine().split(" ");
                String op = command[0];
                
                switch (op) {
                    case "MOVE":
                        int k = Integer.parseInt(command[1]);
                        move(k);
                        break;
                    case "INSERT":
                        int n = Integer.parseInt(command[1]);
                        String str = command[2];
                        insert(str);
                        break;
                    case "DELETE":
                        int delN = Integer.parseInt(command[1]);
                        delete(delN);
                        break;
                    case "GET":
                        int getN = Integer.parseInt(command[1]);
                        out.println(get(getN));
                        break;
                    case "PREV":
                        prev();
                        break;
                    case "NEXT":
                        next();
                        break;
                }
            }
        }
        
        out.flush();
        out.close();
    }
}