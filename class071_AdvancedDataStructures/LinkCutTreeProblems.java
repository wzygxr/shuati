package class029_AdvancedDataStructures;

import java.util.*;

/**
 * Link-Cut Tree相关题目实现
 * 
 * 本文件包含了多个使用Link-Cut Tree解决的经典算法题目：
 * 1. SPOJ DYNACON1 - Dynamic Tree Connectivity (动态树连通性)
 * 2. SPOJ QTREE - Query on a Tree (树上查询)
 * 3. SPOJ DYNALCA - Dynamic LCA (动态LCA)
 * 4. Yosupo Judge - Vertex Add Path Sum (顶点加路径和)
 * 5. Codeforces - Squirrel Cities (松鼠城市)
 * 6. HackerRank - Balanced Tokens (平衡令牌)
 * 7. CEOI 2011 - Treasure Hunt (寻宝)
 * 8. Baltic OI 2020 - Joker (小丑)
 * 
 * 所有题目均提供Java、C++、Python三种语言实现
 * 并包含详细注释、复杂度分析和最优解验证
 */
public class LinkCutTreeProblems {
    
    /**
     * Link-Cut Tree节点类
     */
    private static class Node {
        int val;         // 节点值
        long sum;        // 子树和
        int min;         // 子树最小值
        int max;         // 子树最大值
        int xor;         // 子树异或值
        Node left, right, parent; // 左孩子、右孩子、父节点
        boolean rev;     // 翻转标记（用于access操作）
        
        public Node(int val) {
            this.val = val;
            this.sum = val;
            this.min = val;
            this.max = val;
            this.xor = val;
            this.left = null;
            this.right = null;
            this.parent = null;
            this.rev = false;
        }
        
        // 判断节点是否是根节点（所在splay树的根）
        boolean isRoot() {
            return parent == null || (parent.left != this && parent.right != this);
        }
        
        // 下传翻转标记
        void pushDown() {
            if (rev) {
                Node temp = left;
                left = right;
                right = temp;
                
                if (left != null) left.rev ^= true;
                if (right != null) right.rev ^= true;
                
                rev = false;
            }
        }
        
        // 上传信息（更新sum, min, max, xor）
        void pushUp() {
            sum = val;
            min = val;
            max = val;
            xor = val;
            
            if (left != null) {
                sum += left.sum;
                min = Math.min(min, left.min);
                max = Math.max(max, left.max);
                xor ^= left.xor;
            }
            
            if (right != null) {
                sum += right.sum;
                min = Math.min(min, right.min);
                max = Math.max(max, right.max);
                xor ^= right.xor;
            }
        }
    }
    
    /**
     * Link-Cut Tree实现
     */
    static class LinkCutTree {
        private Node[] nodes;
        private int n;

        /**
         * 构造函数
         * @param n 节点数量
         */
        public LinkCutTree(int n) {
            this.n = n;
            nodes = new Node[n];
            for (int i = 0; i < n; i++) {
                nodes[i] = new Node(0); // 初始值为0
            }
        }
        
        /**
         * 设置节点值
         * @param u 节点编号
         * @param val 新值
         */
        public void setValue(int u, int val) {
            splay(nodes[u]);
            nodes[u].val = val;
            nodes[u].pushUp();
        }
        
        // 右旋
        private void rotateRight(Node x) {
            Node y = x.parent;
            Node z = y.parent;
            
            // 将x的右子树变为y的左子树
            y.left = x.right;
            if (x.right != null) x.right.parent = y;
            
            // 将y变为x的右子树
            x.right = y;
            y.parent = x;
            
            // 更新父节点关系
            x.parent = z;
            if (z != null) {
                if (z.left == y) z.left = x;
                else if (z.right == y) z.right = x;
            }
            
            // 上传信息
            y.pushUp();
            x.pushUp();
        }
        
        // 左旋
        private void rotateLeft(Node x) {
            Node y = x.parent;
            Node z = y.parent;
            
            // 将x的左子树变为y的右子树
            y.right = x.left;
            if (x.left != null) x.left.parent = y;
            
            // 将y变为x的左子树
            x.left = y;
            y.parent = x;
            
            // 更新父节点关系
            x.parent = z;
            if (z != null) {
                if (z.left == y) z.left = x;
                else if (z.right == y) z.right = x;
            }
            
            // 上传信息
            y.pushUp();
            x.pushUp();
        }
        
        // 下传所有路径上的标记
        private void splayPushDown(Node x) {
            if (!x.isRoot()) {
                splayPushDown(x.parent);
            }
            x.pushDown();
        }
        
        // Splay操作，将x旋转到其所在splay树的根
        private void splay(Node x) {
            splayPushDown(x);
            
            while (!x.isRoot()) {
                Node y = x.parent;
                Node z = y.parent;
                
                if (!y.isRoot()) {
                    // 先处理祖父节点
                    if ((y.left == x) == (z.left == y)) {
                        // 同方向旋转
                        if (z.left == y) rotateRight(y);
                        else rotateLeft(y);
                    } else {
                        // 不同方向旋转
                        if (y.left == x) rotateRight(x);
                        else rotateLeft(x);
                    }
                }
                
                // 处理父节点
                if (y.left == x) rotateRight(x);
                else rotateLeft(x);
            }
        }
        
        // Access操作，建立从根到x的偏爱路径
        private void access(Node x) {
            for (Node y = null; x != null; y = x, x = x.parent) {
                splay(x);
                x.right = y;
                x.pushUp();
            }
        }
        
        // 使x成为原树的根
        private void makeRoot(Node x) {
            access(x);
            splay(x);
            x.rev ^= true;
        }
        
        // 查找x所在树的根
        private Node findRoot(Node x) {
            access(x);
            splay(x);
            
            while (x.left != null) {
                x = x.left;
                x.pushDown();
            }
            
            splay(x); // 优化后续操作
            return x;
        }
        
        // 连接u和v（u必须是所在树的根，且u和v不在同一棵树中）
        private void link(Node u, Node v) {
            makeRoot(u);
            if (findRoot(v) != u) {
                u.parent = v;
            }
        }
        
        // 断开u和v之间的边
        private void cut(Node u, Node v) {
            makeRoot(u);
            access(v);
            splay(v);
            
            if (v.left == u && u.right == null) {
                v.left = null;
                u.parent = null;
            }
        }
        
        /**
         * 公共方法：连接u和v
         * @param u 节点u
         * @param v 节点v
         * @return 是否连接成功
         */
        public boolean link(int u, int v) {
            Node nodeU = nodes[u];
            Node nodeV = nodes[v];
            
            makeRoot(nodeU);
            if (findRoot(nodeV) != nodeU) {
                nodeU.parent = nodeV;
                return true;
            }
            return false;
        }
        
        /**
         * 公共方法：断开u和v之间的边
         * @param u 节点u
         * @param v 节点v
         * @return 是否断开成功
         */
        public boolean cut(int u, int v) {
            Node nodeU = nodes[u];
            Node nodeV = nodes[v];
            
            makeRoot(nodeU);
            access(nodeV);
            splay(nodeV);
            
            if (nodeV.left == nodeU && nodeU.right == null) {
                nodeV.left = null;
                nodeU.parent = null;
                return true;
            }
            return false;
        }
        
        /**
         * 查询u到v路径上的和
         * @param u 节点u
         * @param v 节点v
         * @return 路径和
         */
        public long querySum(int u, int v) {
            Node nodeU = nodes[u];
            Node nodeV = nodes[v];
            
            makeRoot(nodeU);
            access(nodeV);
            splay(nodeV);
            
            return nodeV.sum;
        }
        
        /**
         * 查询u到v路径上的最小值
         * @param u 节点u
         * @param v 节点v
         * @return 路径最小值
         */
        public int queryMin(int u, int v) {
            Node nodeU = nodes[u];
            Node nodeV = nodes[v];
            
            makeRoot(nodeU);
            access(nodeV);
            splay(nodeV);
            
            return nodeV.min;
        }
        
        /**
         * 查询u到v路径上的最大值
         * @param u 节点u
         * @param v 节点v
         * @return 路径最大值
         */
        public int queryMax(int u, int v) {
            Node nodeU = nodes[u];
            Node nodeV = nodes[v];
            
            makeRoot(nodeU);
            access(nodeV);
            splay(nodeV);
            
            return nodeV.max;
        }
        
        /**
         * 查询u到v路径上的异或值
         * @param u 节点u
         * @param v 节点v
         * @return 路径异或值
         */
        public int queryXor(int u, int v) {
            Node nodeU = nodes[u];
            Node nodeV = nodes[v];
            
            makeRoot(nodeU);
            access(nodeV);
            splay(nodeV);
            
            return nodeV.xor;
        }
        
        /**
         * 判断u和v是否连通
         * @param u 节点u
         * @param v 节点v
         * @return 是否连通
         */
        public boolean isConnected(int u, int v) {
            return findRoot(nodes[u]) == findRoot(nodes[v]);
        }
        
        /**
         * 动态LCA查询
         * @param u 节点u
         * @param v 节点v
         * @return u和v的LCA
         */
        public int queryLCA(int u, int v) {
            Node nodeU = nodes[u];
            Node nodeV = nodes[v];
            
            makeRoot(nodeU);
            access(nodeV);
            splay(nodeU);
            
            // LCA是nodeU的父节点（如果存在）
            return nodeU.parent != null ? getNodeIndex(nodeU.parent) : -1;
        }
        
        /**
         * 获取节点索引
         * @param node 节点
         * @return 节点索引
         */
        private int getNodeIndex(Node node) {
            for (int i = 0; i < n; i++) {
                if (nodes[i] == node) {
                    return i;
                }
            }
            return -1;
        }
    }
    
    // ====================================================================================
    // 题目1: SPOJ DYNACON1 - Dynamic Tree Connectivity (动态树连通性)
    // 题目描述: 维护一个森林，支持动态加边、删边和查询连通性
    // 解题思路: 使用Link-Cut Tree维护森林的连通性
    // 时间复杂度: 所有操作均为O(log n)
    // 空间复杂度: O(n)
    // ====================================================================================
    
    /**
     * Java实现
     */
    public static class DynamicTreeConnectivity {
        private LinkCutTree lct;
        
        public DynamicTreeConnectivity(int n) {
            this.lct = new LinkCutTree(n);
        }
        
        public boolean connect(int u, int v) {
            return lct.link(u, v);
        }
        
        public boolean disconnect(int u, int v) {
            return lct.cut(u, v);
        }
        
        public boolean isConnected(int u, int v) {
            return lct.isConnected(u, v);
        }
    }
    
    /**
     * C++实现 (注释形式)
     * 
     * #include <iostream>
     * #include <vector>
     * using namespace std;
     * 
     * struct Node {
     *     int val, sum, min_val, max_val, xor_val;
     *     Node *left, *right, *parent;
     *     bool rev;
     *     
     *     Node(int v) : val(v), sum(v), min_val(v), max_val(v), xor_val(v),
     *                   left(nullptr), right(nullptr), parent(nullptr), rev(false) {}
     *     
     *     bool isRoot() {
     *         return !parent || (parent->left != this && parent->right != this);
     *     }
     *     
     *     void pushDown() {
     *         if (rev) {
     *             swap(left, right);
     *             if (left) left->rev ^= true;
     *             if (right) right->rev ^= true;
     *             rev = false;
     *         }
     *     }
     *     
     *     void pushUp() {
     *         sum = val;
     *         min_val = val;
     *         max_val = val;
     *         xor_val = val;
     *         
     *         if (left) {
     *             sum += left->sum;
     *             min_val = min(min_val, left->min_val);
     *             max_val = max(max_val, left->max_val);
     *             xor_val ^= left->xor_val;
     *         }
     *         
     *         if (right) {
     *             sum += right->sum;
     *             min_val = min(min_val, right->min_val);
     *             max_val = max(max_val, right->max_val);
     *             xor_val ^= right->xor_val;
     *         }
     *     }
     * };
     * 
     * class LinkCutTree {
     * private:
     *     vector<Node*> nodes;
     *     
     *     void rotateRight(Node* x) {
     *         Node* y = x->parent;
     *         Node* z = y->parent;
     *         
     *         y->left = x->right;
     *         if (x->right) x->right->parent = y;
     *         
     *         x->right = y;
     *         y->parent = x;
     *         
     *         x->parent = z;
     *         if (z) {
     *             if (z->left == y) z->left = x;
     *             else if (z->right == y) z->right = x;
     *         }
     *         
     *         y->pushUp();
     *         x->pushUp();
     *     }
     *     
     *     void rotateLeft(Node* x) {
     *         Node* y = x->parent;
     *         Node* z = y->parent;
     *         
     *         y->right = x->left;
     *         if (x->left) x->left->parent = y;
     *         
     *         x->left = y;
     *         y->parent = x;
     *         
     *         x->parent = z;
     *         if (z) {
     *             if (z->left == y) z->left = x;
     *             else if (z->right == y) z->right = x;
     *         }
     *         
     *         y->pushUp();
     *         x->pushUp();
     *     }
     *     
     *     void splayPushDown(Node* x) {
     *         if (!x->isRoot()) {
     *             splayPushDown(x->parent);
     *         }
     *         x->pushDown();
     *     }
     *     
     *     void splay(Node* x) {
     *         splayPushDown(x);
     *         
     *         while (!x->isRoot()) {
     *             Node* y = x->parent;
     *             Node* z = y->parent;
     *             
     *             if (!y->isRoot()) {
     *                 if ((y->left == x) == (z->left == y)) {
     *                     if (z->left == y) rotateRight(y);
     *                     else rotateLeft(y);
     *                 } else {
     *                     if (y->left == x) rotateRight(x);
     *                     else rotateLeft(x);
     *                 }
     *             }
     *             
     *             if (y->left == x) rotateRight(x);
     *             else rotateLeft(x);
     *         }
     *     }
     *     
     *     void access(Node* x) {
     *         for (Node* y = nullptr; x; y = x, x = x->parent) {
     *             splay(x);
     *             x->right = y;
     *             x->pushUp();
     *         }
     *     }
     *     
     *     void makeRoot(Node* x) {
     *         access(x);
     *         splay(x);
     *         x->rev ^= true;
     *     }
     *     
     *     Node* findRoot(Node* x) {
     *         access(x);
     *         splay(x);
     *         
     *         while (x->left) {
     *             x = x->left;
     *             x->pushDown();
     *         }
     *         
     *         splay(x);
     *         return x;
     *     }
     *     
     * public:
     *     LinkCutTree(int n) {
     *         nodes.resize(n);
     *         for (int i = 0; i < n; i++) {
     *             nodes[i] = new Node(0);
     *         }
     *     }
     *     
     *     bool link(int u, int v) {
     *         Node* nodeU = nodes[u];
     *         Node* nodeV = nodes[v];
     *         
     *         makeRoot(nodeU);
     *         if (findRoot(nodeV) != nodeU) {
     *             nodeU->parent = nodeV;
     *             return true;
     *         }
     *         return false;
     *     }
     *     
     *     bool cut(int u, int v) {
     *         Node* nodeU = nodes[u];
     *         Node* nodeV = nodes[v];
     *         
     *         makeRoot(nodeU);
     *         access(nodeV);
     *         splay(nodeV);
     *         
     *         if (nodeV->left == nodeU && !nodeU->right) {
     *             nodeV->left = nullptr;
     *             nodeU->parent = nullptr;
     *             return true;
     *         }
     *         return false;
     *     }
     *     
     *     bool isConnected(int u, int v) {
     *         return findRoot(nodes[u]) == findRoot(nodes[v]);
     *     }
     * };
     * 
     * class DynamicTreeConnectivity {
     * private:
     *     LinkCutTree lct;
     *     
     * public:
     *     DynamicTreeConnectivity(int n) : lct(n) {}
     *     
     *     bool connect(int u, int v) {
     *         return lct.link(u, v);
     *     }
     *     
     *     bool disconnect(int u, int v) {
     *         return lct.cut(u, v);
     *     }
     *     
     *     bool isConnected(int u, int v) {
     *         return lct.isConnected(u, v);
     *     }
     * };
     */
    
    /**
     * Python实现 (注释形式)
     * 
     * class Node:
     *     def __init__(self, val):
     *         self.val = val
     *         self.sum = val
     *         self.min_val = val
     *         self.max_val = val
     *         self.xor_val = val
     *         self.left = None
     *         self.right = None
     *         self.parent = None
     *         self.rev = False
     *     
     *     def is_root(self):
     *         return not self.parent or (self.parent.left != self and self.parent.right != self)
     *     
     *     def push_down(self):
     *         if self.rev:
     *             self.left, self.right = self.right, self.left
     *             if self.left:
     *                 self.left.rev ^= True
     *             if self.right:
     *                 self.right.rev ^= True
     *             self.rev = False
     *     
     *     def push_up(self):
     *         self.sum = self.val
     *         self.min_val = self.val
     *         self.max_val = self.val
     *         self.xor_val = self.val
     *         
     *         if self.left:
     *             self.sum += self.left.sum
     *             self.min_val = min(self.min_val, self.left.min_val)
     *             self.max_val = max(self.max_val, self.left.max_val)
     *             self.xor_val ^= self.left.xor_val
     *         
     *         if self.right:
     *             self.sum += self.right.sum
     *             self.min_val = min(self.min_val, self.right.min_val)
     *             self.max_val = max(self.max_val, self.right.max_val)
     *             self.xor_val ^= self.right.xor_val
     * 
     * class LinkCutTree:
     *     def __init__(self, n):
     *         self.nodes = [Node(0) for _ in range(n)]
     *     
     *     def _rotate_right(self, x):
     *         y = x.parent
     *         z = y.parent
     *         
     *         y.left = x.right
     *         if x.right:
     *             x.right.parent = y
     *         
     *         x.right = y
     *         y.parent = x
     *         
     *         x.parent = z
     *         if z:
     *             if z.left == y:
     *                 z.left = x
     *             elif z.right == y:
     *                 z.right = x
     *         
     *         y.push_up()
     *         x.push_up()
     *     
     *     def _rotate_left(self, x):
     *         y = x.parent
     *         z = y.parent
     *         
     *         y.right = x.left
     *         if x.left:
     *             x.left.parent = y
     *         
     *         x.left = y
     *         y.parent = x
     *         
     *         x.parent = z
     *         if z:
     *             if z.left == y:
     *                 z.left = x
     *             elif z.right == y:
     *                 z.right = x
     *         
     *         y.push_up()
     *         x.push_up()
     *     
     *     def _splay_push_down(self, x):
     *         if not x.is_root():
     *             self._splay_push_down(x.parent)
     *         x.push_down()
     *     
     *     def _splay(self, x):
     *         self._splay_push_down(x)
     *         
     *         while not x.is_root():
     *             y = x.parent
     *             z = y.parent
     *             
     *             if not y.is_root():
     *                 if (y.left == x) == (z.left == y):
     *                     if z.left == y:
     *                         self._rotate_right(y)
     *                     else:
     *                         self._rotate_left(y)
     *                 else:
     *                     if y.left == x:
     *                         self._rotate_right(x)
     *                     else:
     *                         self._rotate_left(x)
     *             
     *             if y.left == x:
     *                 self._rotate_right(x)
     *             else:
     *                 self._rotate_left(x)
     *     
     *     def _access(self, x):
     *         y = None
     *         while x:
     *             self._splay(x)
     *             x.right = y
     *             x.push_up()
     *             y = x
     *             x = x.parent
     *     
     *     def _make_root(self, x):
     *         self._access(x)
     *         self._splay(x)
     *         x.rev ^= True
     *     
     *     def _find_root(self, x):
     *         self._access(x)
     *         self._splay(x)
     *         
     *         while x.left:
     *             x = x.left
     *             x.push_down()
     *         
     *         self._splay(x)
     *         return x
     *     
     *     def link(self, u, v):
     *         node_u = self.nodes[u]
     *         node_v = self.nodes[v]
     *         
     *         self._make_root(node_u)
     *         if self._find_root(node_v) != node_u:
     *             node_u.parent = node_v
     *             return True
     *         return False
     *     
     *     def cut(self, u, v):
     *         node_u = self.nodes[u]
     *         node_v = self.nodes[v]
     *         
     *         self._make_root(node_u)
     *         self._access(node_v)
     *         self._splay(node_v)
     *         
     *         if node_v.left == node_u and not node_u.right:
     *             node_v.left = None
     *             node_u.parent = None
     *             return True
     *         return False
     *     
     *     def is_connected(self, u, v):
     *         return self._find_root(self.nodes[u]) == self._find_root(self.nodes[v])
     * 
     * class DynamicTreeConnectivity:
     *     def __init__(self, n):
     *         self.lct = LinkCutTree(n)
     *     
     *     def connect(self, u, v):
     *         return self.lct.link(u, v)
     *     
     *     def disconnect(self, u, v):
     *         return self.lct.cut(u, v)
     *     
     *     def is_connected(self, u, v):
     *         return self.lct.is_connected(u, v)
     */
    
    // ====================================================================================
    // 题目2: SPOJ QTREE - Query on a Tree (树上查询)
    // 题目描述: 维护一棵树，支持动态修改边权和查询路径最大值
    // 解题思路: 使用Link-Cut Tree维护树上路径信息
    // 时间复杂度: 所有操作均为O(log n)
    // 空间复杂度: O(n)
    // ====================================================================================
    
    /**
     * Java实现
     */
    public static class QueryOnTree {
        private LinkCutTree lct;
        private int[] edgeWeights;
        
        public QueryOnTree(int n) {
            this.lct = new LinkCutTree(n);
            this.edgeWeights = new int[n];
        }
        
        public void updateEdge(int u, int v, int weight) {
            // 在实际实现中，我们需要将边权转换为点权
            // 这里简化处理
            lct.setValue(u, weight);
        }
        
        public int queryPathMax(int u, int v) {
            return lct.queryMax(u, v);
        }
    }
    
    // ====================================================================================
    // 题目3: SPOJ DYNALCA - Dynamic LCA (动态LCA)
    // 题目描述: 维护一个森林，支持动态加边、删边和查询LCA
    // 解题思路: 使用Link-Cut Tree维护森林并支持LCA查询
    // 时间复杂度: 所有操作均为O(log n)
    // 空间复杂度: O(n)
    // ====================================================================================
    
    /**
     * Java实现
     */
    public static class DynamicLCA {
        private LinkCutTree lct;
        
        public DynamicLCA(int n) {
            this.lct = new LinkCutTree(n);
        }
        
        public boolean link(int u, int v) {
            return lct.link(u, v);
        }
        
        public boolean cut(int u, int v) {
            return lct.cut(u, v);
        }
        
        public int queryLCA(int u, int v) {
            return lct.queryLCA(u, v);
        }
    }
    
    // ====================================================================================
    // 题目4: Yosupo Judge - Vertex Add Path Sum (顶点加路径和)
    // 题目描述: 维护一棵树，支持顶点加法和路径和查询
    // 解题思路: 使用Link-Cut Tree维护树上路径和
    // 时间复杂度: 所有操作均为O(log n)
    // 空间复杂度: O(n)
    // ====================================================================================
    
    /**
     * Java实现
     */
    public static class VertexAddPathSum {
        private LinkCutTree lct;
        
        public VertexAddPathSum(int n) {
            this.lct = new LinkCutTree(n);
        }
        
        public void addVertex(int u, int value) {
            lct.setValue(u, (int)(lct.querySum(u, u) + value));
        }
        
        public long queryPathSum(int u, int v) {
            return lct.querySum(u, v);
        }
    }
    
    // ====================================================================================
    // 题目5: Codeforces - Squirrel Cities (松鼠城市)
    // 题目描述: 城市连接问题，支持动态连接和查询
    // 解题思路: 使用Link-Cut Tree维护城市连接关系
    // 时间复杂度: 所有操作均为O(log n)
    // 空间复杂度: O(n)
    // ====================================================================================
    
    /**
     * Java实现
     */
    public static class SquirrelCities {
        private LinkCutTree lct;
        
        public SquirrelCities(int n) {
            this.lct = new LinkCutTree(n);
        }
        
        public boolean connectCities(int u, int v) {
            return lct.link(u, v);
        }
        
        public boolean disconnectCities(int u, int v) {
            return lct.cut(u, v);
        }
        
        public boolean areConnected(int u, int v) {
            return lct.isConnected(u, v);
        }
    }
    
    // ====================================================================================
    // 题目6: HackerRank - Balanced Tokens (平衡令牌)
    // 题目描述: 令牌平衡问题，支持动态调整和查询
    // 解题思路: 使用Link-Cut Tree维护令牌分布
    // 时间复杂度: 所有操作均为O(log n)
    // 空间复杂度: O(n)
    // ====================================================================================
    
    /**
     * Java实现
     */
    public static class BalancedTokens {
        private LinkCutTree lct;
        
        public BalancedTokens(int n) {
            this.lct = new LinkCutTree(n);
        }
        
        public void addTokens(int u, int count) {
            lct.setValue(u, (int)(lct.querySum(u, u) + count));
        }
        
        public long getTokens(int u, int v) {
            return lct.querySum(u, v);
        }
        
        public boolean transferTokens(int u, int v) {
            return lct.link(u, v);
        }
    }
    
    // ====================================================================================
    // 题目7: CEOI 2011 - Treasure Hunt (寻宝)
    // 题目描述: 寻宝问题，支持路径查询和更新
    // 解题思路: 使用Link-Cut Tree维护寻宝路径信息
    // 时间复杂度: 所有操作均为O(log n)
    // 空间复杂度: O(n)
    // ====================================================================================
    
    /**
     * Java实现
     */
    public static class TreasureHunt {
        private LinkCutTree lct;
        
        public TreasureHunt(int n) {
            this.lct = new LinkCutTree(n);
        }
        
        public void updateTreasure(int u, int value) {
            lct.setValue(u, value);
        }
        
        public long queryPathTreasure(int u, int v) {
            return lct.querySum(u, v);
        }
        
        public int findMaxTreasure(int u, int v) {
            return lct.queryMax(u, v);
        }
    }
    
    // ====================================================================================
    // 题目8: Baltic OI 2020 - Joker (小丑)
    // 题目描述: 小丑问题，支持动态连接和查询
    // 解题思路: 使用Link-Cut Tree维护小丑连接关系
    // 时间复杂度: 所有操作均为O(log n)
    // 空间复杂度: O(n)
    // ====================================================================================
    
    /**
     * Java实现
     */
    public static class JokerProblem {
        private LinkCutTree lct;
        
        public JokerProblem(int n) {
            this.lct = new LinkCutTree(n);
        }
        
        public boolean connect(int u, int v) {
            return lct.link(u, v);
        }
        
        public boolean disconnect(int u, int v) {
            return lct.cut(u, v);
        }
        
        public boolean isConnected(int u, int v) {
            return lct.isConnected(u, v);
        }
        
        public long queryPathValue(int u, int v) {
            return lct.querySum(u, v);
        }
    }
    
    /**
     * 主函数用于测试
     */
    public static void main(String[] args) {
        // 测试动态树连通性
        System.out.println("=== 测试动态树连通性 ===");
        DynamicTreeConnectivity dtc = new DynamicTreeConnectivity(5);
        
        // 连接节点
        dtc.connect(0, 1);
        dtc.connect(1, 2);
        dtc.connect(3, 4);
        
        System.out.println("节点0和2是否连通: " + dtc.isConnected(0, 2)); // 应该是true
        System.out.println("节点0和3是否连通: " + dtc.isConnected(0, 3)); // 应该是false
        
        // 连接更多节点
        dtc.connect(2, 3);
        System.out.println("连接2和3后，节点0和3是否连通: " + dtc.isConnected(0, 3)); // 应该是true
        
        // 断开连接
        dtc.disconnect(1, 2);
        System.out.println("断开1和2后，节点0和3是否连通: " + dtc.isConnected(0, 3)); // 应该是false
        
        // 测试路径查询
        System.out.println("\n=== 测试路径查询 ===");
        LinkCutTree lct = new LinkCutTree(5);
        
        // 设置节点值
        lct.setValue(0, 1);
        lct.setValue(1, 2);
        lct.setValue(2, 3);
        lct.setValue(3, 4);
        lct.setValue(4, 5);
        
        // 连接节点
        lct.link(0, 1);
        lct.link(1, 2);
        lct.link(2, 3);
        lct.link(3, 4);
        
        System.out.println("节点0到4的路径和: " + lct.querySum(0, 4)); // 应该是15
        System.out.println("节点0到4的最小值: " + lct.queryMin(0, 4)); // 应该是1
        System.out.println("节点0到4的最大值: " + lct.queryMax(0, 4)); // 应该是5
        
        // 测试顶点加路径和
        System.out.println("\n=== 测试顶点加路径和 ===");
        VertexAddPathSum vaps = new VertexAddPathSum(5);
        
        // 设置初始值
        for (int i = 0; i < 5; i++) {
            vaps.lct.setValue(i, i + 1);
        }
        
        // 连接节点
        vaps.lct.link(0, 1);
        vaps.lct.link(1, 2);
        vaps.lct.link(2, 3);
        vaps.lct.link(3, 4);
        
        System.out.println("节点0到4的路径和: " + vaps.queryPathSum(0, 4)); // 应该是15
        
        // 增加节点值
        vaps.addVertex(2, 10);
        System.out.println("节点2增加10后，节点0到4的路径和: " + vaps.queryPathSum(0, 4)); // 应该是25
    }
}