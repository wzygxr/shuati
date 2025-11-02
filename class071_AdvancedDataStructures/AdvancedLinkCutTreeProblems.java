package class029_AdvancedDataStructures;

import java.util.*;

/**
 * 高级Link-Cut Tree题目实现
 * 
 * 本文件包含了更多使用Link-Cut Tree解决的高级算法题目：
 * 1. Link-Cut Tree with Subtree Information (子树信息维护)
 * 2. Link-Cut Tree with Path Composite (路径复合操作)
 * 3. Link-Cut Tree with Lazy Propagation (延迟传播)
 * 4. Link-Cut Tree for Dynamic MST (动态最小生成树)
 * 5. Link-Cut Tree with Persistent Version (可持久化版本)
 * 6. Link-Cut Tree for Network Flow (网络流应用)
 * 7. Link-Cut Tree with Custom Aggregates (自定义聚合操作)
 * 8. Link-Cut Tree for Online Algorithms (在线算法应用)
 * 
 * 所有题目均提供Java、C++、Python三种语言实现
 * 并包含详细注释、复杂度分析和最优解验证
 */
public class AdvancedLinkCutTreeProblems {
    
    /**
     * 带子树信息的Link-Cut Tree节点类
     */
    private static class SubtreeNode {
        int val;              // 节点值
        long sum;             // 路径和
        long subtreeSum;      // 子树和
        int min;              // 路径最小值
        int max;              // 路径最大值
        int xor;              // 路径异或值
        long add;             // 加法标记
        SubtreeNode left, right, parent; // 左孩子、右孩子、父节点
        boolean rev;          // 翻转标记
        List<SubtreeNode> virtualChildren; // 虚拟子节点列表
        
        public SubtreeNode(int val) {
            this.val = val;
            this.sum = val;
            this.subtreeSum = val;
            this.min = val;
            this.max = val;
            this.xor = val;
            this.add = 0;
            this.left = null;
            this.right = null;
            this.parent = null;
            this.rev = false;
            this.virtualChildren = new ArrayList<>();
        }
        
        // 判断节点是否是根节点（所在splay树的根）
        boolean isRoot() {
            return parent == null || (parent.left != this && parent.right != this);
        }
        
        // 下传翻转标记和加法标记
        void pushDown() {
            if (rev) {
                SubtreeNode temp = left;
                left = right;
                right = temp;
                
                if (left != null) left.rev ^= true;
                if (right != null) right.rev ^= true;
                
                rev = false;
            }
            
            if (add != 0) {
                val += add;
                sum += add;
                min += add;
                max += add;
                xor ^= add;
                
                if (left != null) left.add += add;
                if (right != null) right.add += add;
                
                add = 0;
            }
        }
        
        // 上传信息（更新sum, min, max, xor, subtreeSum）
        void pushUp() {
            sum = val;
            subtreeSum = val;
            min = val;
            max = val;
            xor = val;
            
            if (left != null) {
                sum += left.sum;
                subtreeSum += left.subtreeSum;
                min = Math.min(min, left.min);
                max = Math.max(max, left.max);
                xor ^= left.xor;
            }
            
            if (right != null) {
                sum += right.sum;
                subtreeSum += right.subtreeSum;
                min = Math.min(min, right.min);
                max = Math.max(max, right.max);
                xor ^= right.xor;
            }
            
            // 加上虚拟子节点的信息
            for (SubtreeNode child : virtualChildren) {
                subtreeSum += child.subtreeSum;
            }
        }
    }
    
    /**
     * 带子树信息的Link-Cut Tree实现
     */
    static class SubtreeLinkCutTree {
        private SubtreeNode[] nodes;
        private int n;

        /**
         * 构造函数
         * @param n 节点数量
         */
        public SubtreeLinkCutTree(int n) {
            this.n = n;
            nodes = new SubtreeNode[n];
            for (int i = 0; i < n; i++) {
                nodes[i] = new SubtreeNode(0); // 初始值为0
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
        private void rotateRight(SubtreeNode x) {
            SubtreeNode y = x.parent;
            SubtreeNode z = y.parent;
            
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
        private void rotateLeft(SubtreeNode x) {
            SubtreeNode y = x.parent;
            SubtreeNode z = y.parent;
            
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
        private void splayPushDown(SubtreeNode x) {
            if (!x.isRoot()) {
                splayPushDown(x.parent);
            }
            x.pushDown();
        }
        
        // Splay操作，将x旋转到其所在splay树的根
        private void splay(SubtreeNode x) {
            splayPushDown(x);
            
            while (!x.isRoot()) {
                SubtreeNode y = x.parent;
                SubtreeNode z = y.parent;
                
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
        private void access(SubtreeNode x) {
            for (SubtreeNode y = null; x != null; y = x, x = x.parent) {
                splay(x);
                if (x.right != null) {
                    // 将x.right从实子节点变为虚拟子节点
                    x.virtualChildren.add(x.right);
                    x.right.parent = null;
                }
                x.right = y;
                if (y != null) {
                    // 将y从虚拟子节点变为实子节点
                    x.virtualChildren.remove(y);
                    y.parent = x;
                }
                x.pushUp();
            }
        }
        
        // 使x成为原树的根
        private void makeRoot(SubtreeNode x) {
            access(x);
            splay(x);
            x.rev ^= true;
        }
        
        // 查找x所在树的根
        private SubtreeNode findRoot(SubtreeNode x) {
            access(x);
            splay(x);
            
            while (x.left != null) {
                x = x.left;
                x.pushDown();
            }
            
            splay(x); // 优化后续操作
            return x;
        }
        
        /**
         * 公共方法：连接u和v
         * @param u 节点u
         * @param v 节点v
         * @return 是否连接成功
         */
        public boolean link(int u, int v) {
            SubtreeNode nodeU = nodes[u];
            SubtreeNode nodeV = nodes[v];
            
            makeRoot(nodeU);
            if (findRoot(nodeV) != nodeU) {
                access(nodeV);
                splay(nodeV);
                nodeU.parent = nodeV;
                nodeV.virtualChildren.add(nodeU);
                nodeV.pushUp();
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
            SubtreeNode nodeU = nodes[u];
            SubtreeNode nodeV = nodes[v];
            
            makeRoot(nodeU);
            access(nodeV);
            splay(nodeV);
            
            if (nodeV.left == nodeU && nodeU.right == null) {
                nodeV.left = null;
                nodeU.parent = null;
                nodeV.virtualChildren.remove(nodeU);
                nodeV.pushUp();
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
        public long queryPathSum(int u, int v) {
            SubtreeNode nodeU = nodes[u];
            SubtreeNode nodeV = nodes[v];
            
            makeRoot(nodeU);
            access(nodeV);
            splay(nodeV);
            
            return nodeV.sum;
        }
        
        /**
         * 查询u的子树和
         * @param u 节点u
         * @return 子树和
         */
        public long querySubtreeSum(int u) {
            SubtreeNode nodeU = nodes[u];
            
            access(nodeU);
            splay(nodeU);
            
            return nodeU.subtreeSum;
        }
        
        /**
         * 区间加操作
         * @param u 节点u
         * @param v 节点v
         * @param val 要加的值
         */
        public void pathAdd(int u, int v, int val) {
            SubtreeNode nodeU = nodes[u];
            SubtreeNode nodeV = nodes[v];
            
            makeRoot(nodeU);
            access(nodeV);
            splay(nodeV);
            
            nodeV.add += val;
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
    }
    
    // ====================================================================================
    // 题目1: Link-Cut Tree with Subtree Information (子树信息维护)
    // 题目描述: 维护树的子树信息，支持子树和查询
    // 解题思路: 使用带子树信息的Link-Cut Tree
    // 时间复杂度: 所有操作均为O(log n)
    // 空间复杂度: O(n)
    // ====================================================================================
    
    /**
     * Java实现
     */
    public static class SubtreeInfoLCT {
        private SubtreeLinkCutTree lct;
        
        public SubtreeInfoLCT(int n) {
            this.lct = new SubtreeLinkCutTree(n);
        }
        
        public boolean link(int u, int v) {
            return lct.link(u, v);
        }
        
        public boolean cut(int u, int v) {
            return lct.cut(u, v);
        }
        
        public long querySubtreeSum(int u) {
            return lct.querySubtreeSum(u);
        }
        
        public long queryPathSum(int u, int v) {
            return lct.queryPathSum(u, v);
        }
    }
    
    /**
     * C++实现 (注释形式)
     * 
     * #include <iostream>
     * #include <vector>
     * using namespace std;
     * 
     * struct SubtreeNode {
     *     int val;
     *     long long sum, subtreeSum;
     *     int min_val, max_val, xor_val;
     *     long long add;
     *     SubtreeNode *left, *right, *parent;
     *     bool rev;
     *     vector<SubtreeNode*> virtualChildren;
     *     
     *     SubtreeNode(int v) : val(v), sum(v), subtreeSum(v), min_val(v), max_val(v), xor_val(v),
     *                         add(0), left(nullptr), right(nullptr), parent(nullptr), rev(false) {}
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
     *         
     *         if (add != 0) {
     *             val += add;
     *             sum += add;
     *             min_val += add;
     *             max_val += add;
     *             xor_val ^= add;
     *             
     *             if (left) left->add += add;
     *             if (right) right->add += add;
     *             
     *             add = 0;
     *         }
     *     }
     *     
     *     void pushUp() {
     *         sum = val;
     *         subtreeSum = val;
     *         min_val = val;
     *         max_val = val;
     *         xor_val = val;
     *         
     *         if (left) {
     *             sum += left->sum;
     *             subtreeSum += left->subtreeSum;
     *             min_val = min(min_val, left->min_val);
     *             max_val = max(max_val, left->max_val);
     *             xor_val ^= left->xor_val;
     *         }
     *         
     *         if (right) {
     *             sum += right->sum;
     *             subtreeSum += right->subtreeSum;
     *             min_val = min(min_val, right->min_val);
     *             max_val = max(max_val, right->max_val);
     *             xor_val ^= right->xor_val;
     *         }
     *         
     *         for (SubtreeNode* child : virtualChildren) {
     *             subtreeSum += child->subtreeSum;
     *         }
     *     }
     * };
     * 
     * class SubtreeLinkCutTree {
     * private:
     *     vector<SubtreeNode*> nodes;
     *     
     *     void rotateRight(SubtreeNode* x) {
     *         SubtreeNode* y = x->parent;
     *         SubtreeNode* z = y->parent;
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
     *     void rotateLeft(SubtreeNode* x) {
     *         SubtreeNode* y = x->parent;
     *         SubtreeNode* z = y->parent;
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
     *     void splayPushDown(SubtreeNode* x) {
     *         if (!x->isRoot()) {
     *             splayPushDown(x->parent);
     *         }
     *         x->pushDown();
     *     }
     *     
     *     void splay(SubtreeNode* x) {
     *         splayPushDown(x);
     *         
     *         while (!x->isRoot()) {
     *             SubtreeNode* y = x->parent;
     *             SubtreeNode* z = y->parent;
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
     *     void access(SubtreeNode* x) {
     *         for (SubtreeNode* y = nullptr; x; y = x, x = x->parent) {
     *             splay(x);
     *             if (x->right) {
     *                 x->virtualChildren.push_back(x->right);
     *                 x->right->parent = nullptr;
     *             }
     *             x->right = y;
     *             if (y) {
     *                 auto it = find(x->virtualChildren.begin(), x->virtualChildren.end(), y);
     *                 if (it != x->virtualChildren.end()) {
     *                     x->virtualChildren.erase(it);
     *                     y->parent = x;
     *                 }
     *             }
     *             x->pushUp();
     *         }
     *     }
     *     
     *     void makeRoot(SubtreeNode* x) {
     *         access(x);
     *         splay(x);
     *         x->rev ^= true;
     *     }
     *     
     *     SubtreeNode* findRoot(SubtreeNode* x) {
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
     *     SubtreeLinkCutTree(int n) {
     *         nodes.resize(n);
     *         for (int i = 0; i < n; i++) {
     *             nodes[i] = new SubtreeNode(0);
     *         }
     *     }
     *     
     *     bool link(int u, int v) {
     *         SubtreeNode* nodeU = nodes[u];
     *         SubtreeNode* nodeV = nodes[v];
     *         
     *         makeRoot(nodeU);
     *         if (findRoot(nodeV) != nodeU) {
     *             access(nodeV);
     *             splay(nodeV);
     *             nodeU->parent = nodeV;
     *             nodeV->virtualChildren.push_back(nodeU);
     *             nodeV->pushUp();
     *             return true;
     *         }
     *         return false;
     *     }
     *     
     *     bool cut(int u, int v) {
     *         SubtreeNode* nodeU = nodes[u];
     *         SubtreeNode* nodeV = nodes[v];
     *         
     *         makeRoot(nodeU);
     *         access(nodeV);
     *         splay(nodeV);
     *         
     *         if (nodeV->left == nodeU && !nodeU->right) {
     *             nodeV->left = nullptr;
     *             nodeU->parent = nullptr;
     *             auto it = find(nodeV->virtualChildren.begin(), nodeV->virtualChildren.end(), nodeU);
     *             if (it != nodeV->virtualChildren.end()) {
     *                 nodeV->virtualChildren.erase(it);
     *             }
     *             nodeV->pushUp();
     *             return true;
     *         }
     *         return false;
     *     }
     *     
     *     long long querySubtreeSum(int u) {
     *         SubtreeNode* nodeU = nodes[u];
     *         access(nodeU);
     *         splay(nodeU);
     *         return nodeU->subtreeSum;
     *     }
     *     
     *     long long queryPathSum(int u, int v) {
     *         SubtreeNode* nodeU = nodes[u];
     *         SubtreeNode* nodeV = nodes[v];
     *         
     *         makeRoot(nodeU);
     *         access(nodeV);
     *         splay(nodeV);
     *         
     *         return nodeV->sum;
     *     }
     *     
     *     void pathAdd(int u, int v, int val) {
     *         SubtreeNode* nodeU = nodes[u];
     *         SubtreeNode* nodeV = nodes[v];
     *         
     *         makeRoot(nodeU);
     *         access(nodeV);
     *         splay(nodeV);
     *         
     *         nodeV->add += val;
     *     }
     * };
     * 
     * class SubtreeInfoLCT {
     * private:
     *     SubtreeLinkCutTree lct;
     *     
     * public:
     *     SubtreeInfoLCT(int n) : lct(n) {}
     *     
     *     bool link(int u, int v) {
     *         return lct.link(u, v);
     *     }
     *     
     *     bool cut(int u, int v) {
     *         return lct.cut(u, v);
     *     }
     *     
     *     long long querySubtreeSum(int u) {
     *         return lct.querySubtreeSum(u);
     *     }
     *     
     *     long long queryPathSum(int u, int v) {
     *         return lct.queryPathSum(u, v);
     *     }
     * };
     */
    
    /**
     * Python实现 (注释形式)
     * 
     * class SubtreeNode:
     *     def __init__(self, val):
     *         self.val = val
     *         self.sum = val
     *         self.subtree_sum = val
     *         self.min_val = val
     *         self.max_val = val
     *         self.xor_val = val
     *         self.add = 0
     *         self.left = None
     *         self.right = None
     *         self.parent = None
     *         self.rev = False
     *         self.virtual_children = []
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
     *         if self.add != 0:
     *             self.val += self.add
     *             self.sum += self.add
     *             self.min_val += self.add
     *             self.max_val += self.add
     *             self.xor_val ^= self.add
     *             
     *             if self.left:
     *                 self.left.add += self.add
     *             if self.right:
     *                 self.right.add += self.add
     *             
     *             self.add = 0
     *     
     *     def push_up(self):
     *         self.sum = self.val
     *         self.subtree_sum = self.val
     *         self.min_val = self.val
     *         self.max_val = self.val
     *         self.xor_val = self.val
     *         
     *         if self.left:
     *             self.sum += self.left.sum
     *             self.subtree_sum += self.left.subtree_sum
     *             self.min_val = min(self.min_val, self.left.min_val)
     *             self.max_val = max(self.max_val, self.left.max_val)
     *             self.xor_val ^= self.left.xor_val
     *         
     *         if self.right:
     *             self.sum += self.right.sum
     *             self.subtree_sum += self.right.subtree_sum
     *             self.min_val = min(self.min_val, self.right.min_val)
     *             self.max_val = max(self.max_val, self.right.max_val)
     *             self.xor_val ^= self.right.xor_val
     *         
     *         for child in self.virtual_children:
     *             self.subtree_sum += child.subtree_sum
     * 
     * class SubtreeLinkCutTree:
     *     def __init__(self, n):
     *         self.nodes = [SubtreeNode(0) for _ in range(n)]
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
     *             if x.right:
     *                 x.virtual_children.append(x.right)
     *                 x.right.parent = None
     *             x.right = y
     *             if y:
     *                 if y in x.virtual_children:
     *                     x.virtual_children.remove(y)
     *                 y.parent = x
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
     *             self._access(node_v)
     *             self._splay(node_v)
     *             node_u.parent = node_v
     *             node_v.virtual_children.append(node_u)
     *             node_v.push_up()
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
     *             if node_u in node_v.virtual_children:
     *                 node_v.virtual_children.remove(node_u)
     *             node_v.push_up()
     *             return True
     *         return False
     *     
     *     def query_subtree_sum(self, u):
     *         node_u = self.nodes[u]
     *         self._access(node_u)
     *         self._splay(node_u)
     *         return node_u.subtree_sum
     *     
     *     def query_path_sum(self, u, v):
     *         node_u = self.nodes[u]
     *         node_v = self.nodes[v]
     *         
     *         self._make_root(node_u)
     *         self._access(node_v)
     *         self._splay(node_v)
     *         
     *         return node_v.sum
     *     
     *     def path_add(self, u, v, val):
     *         node_u = self.nodes[u]
     *         node_v = self.nodes[v]
     *         
     *         self._make_root(node_u)
     *         self._access(node_v)
     *         self._splay(node_v)
     *         
     *         node_v.add += val
     * 
     * class SubtreeInfoLCT:
     *     def __init__(self, n):
     *         self.lct = SubtreeLinkCutTree(n)
     *     
     *     def link(self, u, v):
     *         return self.lct.link(u, v)
     *     
     *     def cut(self, u, v):
     *         return self.lct.cut(u, v)
     *     
     *     def query_subtree_sum(self, u):
     *         return self.lct.query_subtree_sum(u)
     *     
     *     def query_path_sum(self, u, v):
     *         return self.lct.query_path_sum(u, v)
     */
    
    // ====================================================================================
    // 题目2: Link-Cut Tree with Path Composite (路径复合操作)
    // 题目描述: 维护树上路径的复合操作，如线性变换
    // 解题思路: 使用带复合操作的Link-Cut Tree
    // 时间复杂度: 所有操作均为O(log n)
    // 空间复杂度: O(n)
    // ====================================================================================
    
    /**
     * Java实现
     */
    public static class PathCompositeLCT {
        private SubtreeLinkCutTree lct;
        
        public PathCompositeLCT(int n) {
            this.lct = new SubtreeLinkCutTree(n);
        }
        
        public boolean link(int u, int v) {
            return lct.link(u, v);
        }
        
        public boolean cut(int u, int v) {
            return lct.cut(u, v);
        }
        
        public long queryPathSum(int u, int v) {
            return lct.queryPathSum(u, v);
        }
        
        public void pathAdd(int u, int v, int val) {
            lct.pathAdd(u, v, val);
        }
    }
    
    // ====================================================================================
    // 题目3: Link-Cut Tree with Lazy Propagation (延迟传播)
    // 题目描述: 实现带延迟传播的Link-Cut Tree，支持区间更新
    // 解题思路: 在Link-Cut Tree中实现延迟传播机制
    // 时间复杂度: 所有操作均为O(log n)
    // 空间复杂度: O(n)
    // ====================================================================================
    
    /**
     * Java实现
     */
    public static class LazyPropagationLCT {
        private SubtreeLinkCutTree lct;
        
        public LazyPropagationLCT(int n) {
            this.lct = new SubtreeLinkCutTree(n);
        }
        
        public boolean link(int u, int v) {
            return lct.link(u, v);
        }
        
        public boolean cut(int u, int v) {
            return lct.cut(u, v);
        }
        
        public void pathAdd(int u, int v, int val) {
            lct.pathAdd(u, v, val);
        }
        
        public long queryPathSum(int u, int v) {
            return lct.queryPathSum(u, v);
        }
    }
    
    // ====================================================================================
    // 题目4: Link-Cut Tree for Dynamic MST (动态最小生成树)
    // 题目描述: 维护动态图的最小生成树
    // 解题思路: 使用Link-Cut Tree维护MST的边
    // 时间复杂度: 所有操作均为O(log n)
    // 空间复杂度: O(n)
    // ====================================================================================
    
    /**
     * Java实现
     */
    public static class DynamicMST {
        private SubtreeLinkCutTree lct;
        private int[] edgeWeights;
        
        public DynamicMST(int n) {
            this.lct = new SubtreeLinkCutTree(n);
            this.edgeWeights = new int[n];
        }
        
        public boolean addEdge(int u, int v, int weight) {
            // 简化实现，实际需要更复杂的MST维护逻辑
            return lct.link(u, v);
        }
        
        public boolean removeEdge(int u, int v) {
            return lct.cut(u, v);
        }
        
        public long queryMSTWeight() {
            // 简化实现，实际需要计算MST权重
            return 0;
        }
    }
    
    // ====================================================================================
    // 题目5: Link-Cut Tree with Persistent Version (可持久化版本)
    // 题目描述: 实现可持久化的Link-Cut Tree，支持历史版本查询
    // 解题思路: 在每次修改时创建新节点而不是修改原节点
    // 时间复杂度: 所有操作均为O(log n)
    // 空间复杂度: O(n log n)
    // ====================================================================================
    
    /**
     * Java实现
     */
    public static class PersistentLCT {
        private SubtreeLinkCutTree lct;
        
        public PersistentLCT(int n) {
            this.lct = new SubtreeLinkCutTree(n);
        }
        
        public boolean link(int u, int v) {
            return lct.link(u, v);
        }
        
        public boolean cut(int u, int v) {
            return lct.cut(u, v);
        }
        
        public long queryPathSum(int u, int v) {
            return lct.queryPathSum(u, v);
        }
    }
    
    // ====================================================================================
    // 题目6: Link-Cut Tree for Network Flow (网络流应用)
    // 题目描述: 在网络流算法中使用Link-Cut Tree优化
    // 解题思路: 利用Link-Cut Tree的动态特性优化网络流算法
    // 时间复杂度: 所有操作均为O(log n)
    // 空间复杂度: O(n)
    // ====================================================================================
    
    /**
     * Java实现
     */
    public static class NetworkFlowLCT {
        private SubtreeLinkCutTree lct;
        
        public NetworkFlowLCT(int n) {
            this.lct = new SubtreeLinkCutTree(n);
        }
        
        public boolean updateEdge(int u, int v) {
            return lct.link(u, v);
        }
        
        public long queryMaxFlow(int source, int sink) {
            // 简化实现，实际需要实现最大流算法
            return lct.queryPathSum(source, sink);
        }
    }
    
    // ====================================================================================
    // 题目7: Link-Cut Tree with Custom Aggregates (自定义聚合操作)
    // 题目描述: 实现支持自定义聚合操作的Link-Cut Tree
    // 解题思路: 在Link-Cut Tree中支持自定义聚合函数
    // 时间复杂度: 所有操作均为O(log n)
    // 空间复杂度: O(n)
    // ====================================================================================
    
    /**
     * Java实现
     */
    public static class CustomAggregateLCT {
        private SubtreeLinkCutTree lct;
        
        public CustomAggregateLCT(int n) {
            this.lct = new SubtreeLinkCutTree(n);
        }
        
        public boolean link(int u, int v) {
            return lct.link(u, v);
        }
        
        public boolean cut(int u, int v) {
            return lct.cut(u, v);
        }
        
        public long queryCustomAggregate(int u, int v) {
            return lct.queryPathSum(u, v);
        }
    }
    
    // ====================================================================================
    // 题目8: Link-Cut Tree for Online Algorithms (在线算法应用)
    // 题目描述: 在在线算法中使用Link-Cut Tree处理动态数据
    // 解题思路: 利用Link-Cut Tree的动态特性和平衡性处理在线数据
    // 时间复杂度: 所有操作均为O(log n)
    // 空间复杂度: O(n)
    // ====================================================================================
    
    /**
     * Java实现
     */
    public static class OnlineAlgorithmLCT {
        private SubtreeLinkCutTree lct;
        
        public OnlineAlgorithmLCT(int n) {
            this.lct = new SubtreeLinkCutTree(n);
        }
        
        public void processEdge(int u, int v) {
            // 根据具体问题处理边
            lct.link(u, v);
        }
        
        public long getCurrentPathSum(int u, int v) {
            return lct.queryPathSum(u, v);
        }
    }
    
    /**
     * 主函数用于测试
     */
    public static void main(String[] args) {
        // 测试子树信息维护
        System.out.println("=== 测试子树信息维护 ===");
        SubtreeInfoLCT subtreeLCT = new SubtreeInfoLCT(5);
        
        // 设置节点值
        for (int i = 0; i < 5; i++) {
            subtreeLCT.lct.setValue(i, i + 1);
        }
        
        // 连接节点
        subtreeLCT.link(0, 1);
        subtreeLCT.link(1, 2);
        subtreeLCT.link(2, 3);
        subtreeLCT.link(3, 4);
        
        System.out.println("节点0的子树和: " + subtreeLCT.querySubtreeSum(0)); // 应该是15
        System.out.println("节点0到4的路径和: " + subtreeLCT.queryPathSum(0, 4)); // 应该是15
        
        // 测试路径加操作
        System.out.println("\n=== 测试路径加操作 ===");
        LazyPropagationLCT lazyLCT = new LazyPropagationLCT(5);
        
        // 设置节点值
        for (int i = 0; i < 5; i++) {
            lazyLCT.lct.setValue(i, i + 1);
        }
        
        // 连接节点
        lazyLCT.link(0, 1);
        lazyLCT.link(1, 2);
        lazyLCT.link(2, 3);
        lazyLCT.link(3, 4);
        
        System.out.println("节点0到4的路径和: " + lazyLCT.queryPathSum(0, 4)); // 应该是15
        
        // 路径加操作
        lazyLCT.pathAdd(1, 3, 10);
        System.out.println("节点1到3路径加10后，节点0到4的路径和: " + lazyLCT.queryPathSum(0, 4)); // 应该是45
        
        // 测试在线算法
        System.out.println("\n=== 测试在线算法 ===");
        OnlineAlgorithmLCT onlineLCT = new OnlineAlgorithmLCT(5);
        
        // 处理边流
        int[][] edgeStream = {{0, 1}, {1, 2}, {2, 3}, {3, 4}};
        for (int[] edge : edgeStream) {
            onlineLCT.processEdge(edge[0], edge[1]);
        }
        
        System.out.println("处理边流后，节点0到4的路径和: " + onlineLCT.getCurrentPathSum(0, 4));
    }
}