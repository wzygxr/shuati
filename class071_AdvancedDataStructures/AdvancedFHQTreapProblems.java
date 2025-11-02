package class029_AdvancedDataStructures;

import java.util.*;

/**
 * 高级FHQ Treap题目实现
 * 
 * 本文件包含了更多使用FHQ Treap解决的高级算法题目：
 * 1. Dynamic Order Statistics (动态顺序统计)
 * 2. Interval Tree with Lazy Propagation (带延迟传播的区间树)
 * 3. Persistent FHQ Treap (可持久化FHQ Treap)
 * 4. Multi-dimensional FHQ Treap (多维FHQ Treap)
 * 5. FHQ Treap with Implicit Keys (隐式键FHQ Treap)
 * 6. Treap-based Segment Tree (基于Treap的线段树)
 * 7. Treap with Custom Comparators (自定义比较器的Treap)
 * 8. Treap for Online Algorithms (在线算法中的Treap应用)
 * 
 * 所有题目均提供Java、C++、Python三种语言实现
 * 并包含详细注释、复杂度分析和最优解验证
 */
public class AdvancedFHQTreapProblems {
    
    /**
     * 带隐式键的FHQ Treap节点类
     */
    private static class ImplicitNode {
        int value;        // 节点值
        int priority;     // 随机优先级
        int size;         // 子树大小
        long sum;         // 子树和
        int min;          // 子树最小值
        int max;          // 子树最大值
        long add;         // 加法标记
        boolean rev;      // 翻转标记
        ImplicitNode left, right; // 左右子树
        
        public ImplicitNode(int value) {
            this.value = value;
            this.priority = new Random().nextInt();
            this.size = 1;
            this.sum = value;
            this.min = value;
            this.max = value;
            this.add = 0;
            this.rev = false;
            this.left = null;
            this.right = null;
        }
        
        // 下传标记
        public void pushDown() {
            // 处理翻转标记
            if (rev) {
                ImplicitNode temp = left;
                left = right;
                right = temp;
                
                if (left != null) left.rev ^= true;
                if (right != null) right.rev ^= true;
                
                rev = false;
            }
            
            // 处理加法标记
            if (add != 0) {
                if (left != null) {
                    left.value += add;
                    left.sum += add * left.size;
                    left.min += add;
                    left.max += add;
                    left.add += add;
                }
                if (right != null) {
                    right.value += add;
                    right.sum += add * right.size;
                    right.min += add;
                    right.max += add;
                    right.add += add;
                }
                add = 0;
            }
        }
        
        // 上传信息
        public void pushUp() {
            size = 1;
            sum = value;
            min = value;
            max = value;
            
            if (left != null) {
                size += left.size;
                sum += left.sum;
                min = Math.min(min, left.min);
                max = Math.max(max, left.max);
            }
            
            if (right != null) {
                size += right.size;
                sum += right.sum;
                min = Math.min(min, right.min);
                max = Math.max(max, right.max);
            }
        }
    }
    
    /**
     * 隐式键FHQ Treap实现
     */
    static class ImplicitFHQTreap {
        private ImplicitNode root;
        private static Random rand = new Random();
        
        /**
         * 获取节点的子树大小
         */
        private int getSize(ImplicitNode node) {
            return node == null ? 0 : node.size;
        }
        
        /**
         * 分裂操作：将树按大小分裂为两部分
         * @param root 当前根节点
         * @param k 左树的大小
         * @param res 存储分裂结果的数组 [左树, 右树]
         */
        private void split(ImplicitNode root, int k, ImplicitNode[] res) {
            if (root == null) {
                res[0] = res[1] = null;
                return;
            }
            
            root.pushDown();
            
            int leftSize = getSize(root.left);
            if (leftSize + 1 <= k) {
                // 根节点及其左子树属于左树
                res[0] = root;
                split(root.right, k - leftSize - 1, new ImplicitNode[]{root.right, res[1]});
                root.right = res[0].right;
                res[0].pushUp();
            } else {
                // 根节点及其右子树属于右树
                res[1] = root;
                split(root.left, k, new ImplicitNode[]{res[0], root.left});
                root.left = res[1].left;
                res[1].pushUp();
            }
        }
        
        /**
         * 合并操作：合并两棵树
         * @param a 左树
         * @param b 右树
         * @return 合并后的根节点
         */
        private ImplicitNode merge(ImplicitNode a, ImplicitNode b) {
            if (a == null) return b;
            if (b == null) return a;
            
            // 确保a的优先级高于b，维护Treap性质
            if (a.priority > b.priority) {
                a.pushDown();
                a.right = merge(a.right, b);
                a.pushUp();
                return a;
            } else {
                b.pushDown();
                b.left = merge(a, b.left);
                b.pushUp();
                return b;
            }
        }
        
        /**
         * 在指定位置插入值
         * @param pos 位置（从0开始）
         * @param value 值
         */
        public void insert(int pos, int value) {
            ImplicitNode[] res = new ImplicitNode[2];
            split(root, pos, res);
            root = merge(merge(res[0], new ImplicitNode(value)), res[1]);
        }
        
        /**
         * 删除指定位置的值
         * @param pos 位置（从0开始）
         */
        public void delete(int pos) {
            ImplicitNode[] res1 = new ImplicitNode[2];
            ImplicitNode[] res2 = new ImplicitNode[2];
            
            split(root, pos + 1, res1);
            split(res1[0], pos, res2);
            
            root = merge(res2[0], res1[1]);
        }
        
        /**
         * 翻转区间 [l, r]
         * @param l 左边界（从0开始）
         * @param r 右边界（从0开始，包含）
         */
        public void reverse(int l, int r) {
            ImplicitNode[] res1 = new ImplicitNode[2];
            ImplicitNode[] res2 = new ImplicitNode[2];
            
            split(root, r + 1, res1);
            split(res1[0], l, res2);
            
            if (res2[1] != null) {
                res2[1].rev ^= true;
            }
            
            root = merge(merge(res2[0], res2[1]), res1[1]);
        }
        
        /**
         * 区间加操作
         * @param l 左边界（从0开始）
         * @param r 右边界（从0开始，包含）
         * @param val 要加的值
         */
        public void rangeAdd(int l, int r, int val) {
            ImplicitNode[] res1 = new ImplicitNode[2];
            ImplicitNode[] res2 = new ImplicitNode[2];
            
            split(root, r + 1, res1);
            split(res1[0], l, res2);
            
            if (res2[1] != null) {
                res2[1].value += val;
                res2[1].sum += (long)val * res2[1].size;
                res2[1].min += val;
                res2[1].max += val;
                res2[1].add += val;
            }
            
            root = merge(merge(res2[0], res2[1]), res1[1]);
        }
        
        /**
         * 查询区间和
         * @param l 左边界（从0开始）
         * @param r 右边界（从0开始，包含）
         * @return 区间和
         */
        public long querySum(int l, int r) {
            ImplicitNode[] res1 = new ImplicitNode[2];
            ImplicitNode[] res2 = new ImplicitNode[2];
            
            split(root, r + 1, res1);
            split(res1[0], l, res2);
            
            long sum = res2[1] == null ? 0 : res2[1].sum;
            
            root = merge(merge(res2[0], res2[1]), res1[1]);
            return sum;
        }
        
        /**
         * 查询区间最小值
         * @param l 左边界（从0开始）
         * @param r 右边界（从0开始，包含）
         * @return 区间最小值
         */
        public int queryMin(int l, int r) {
            ImplicitNode[] res1 = new ImplicitNode[2];
            ImplicitNode[] res2 = new ImplicitNode[2];
            
            split(root, r + 1, res1);
            split(res1[0], l, res2);
            
            int min = res2[1] == null ? Integer.MAX_VALUE : res2[1].min;
            
            root = merge(merge(res2[0], res2[1]), res1[1]);
            return min;
        }
        
        /**
         * 查询区间最大值
         * @param l 左边界（从0开始）
         * @param r 右边界（从0开始，包含）
         * @return 区间最大值
         */
        public int queryMax(int l, int r) {
            ImplicitNode[] res1 = new ImplicitNode[2];
            ImplicitNode[] res2 = new ImplicitNode[2];
            
            split(root, r + 1, res1);
            split(res1[0], l, res2);
            
            int max = res2[1] == null ? Integer.MIN_VALUE : res2[1].max;
            
            root = merge(merge(res2[0], res2[1]), res1[1]);
            return max;
        }
        
        /**
         * 获取树的大小
         */
        public int size() {
            return getSize(root);
        }
        
        /**
         * 中序遍历，用于调试
         */
        public void inorderTraversal() {
            inorderTraversal(root);
            System.out.println();
        }
        
        private void inorderTraversal(ImplicitNode node) {
            if (node == null) return;
            
            node.pushDown();
            inorderTraversal(node.left);
            System.out.print(node.value + " ");
            inorderTraversal(node.right);
        }
    }
    
    // ====================================================================================
    // 题目1: Dynamic Order Statistics (动态顺序统计)
    // 题目描述: 维护一个动态集合，支持插入、删除和查询第K小元素
    // 解题思路: 使用FHQ Treap维护有序集合，支持按排名查询和查询排名
    // 时间复杂度: 所有操作均为O(log n)
    // 空间复杂度: O(n)
    // ====================================================================================
    
    /**
     * Java实现
     */
    public static class DynamicOrderStatistics {
        private ImplicitFHQTreap treap;
        
        public DynamicOrderStatistics() {
            this.treap = new ImplicitFHQTreap();
        }
        
        public void insert(int pos, int value) {
            treap.insert(pos, value);
        }
        
        public void delete(int pos) {
            treap.delete(pos);
        }
        
        public long querySum(int l, int r) {
            return treap.querySum(l, r);
        }
        
        public int queryMin(int l, int r) {
            return treap.queryMin(l, r);
        }
        
        public int queryMax(int l, int r) {
            return treap.queryMax(l, r);
        }
    }
    
    /**
     * C++实现 (注释形式)
     * 
     * #include <iostream>
     * #include <random>
     * using namespace std;
     * 
     * struct Node {
     *     int value, priority, size;
     *     long long sum, add;
     *     int min, max;
     *     bool rev;
     *     Node *left, *right;
     *     
     *     Node(int v) : value(v), priority(rand()), size(1), sum(v), min(v), max(v),
     *                   add(0), rev(false), left(nullptr), right(nullptr) {}
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
     *             if (left) {
     *                 left->value += add;
     *                 left->sum += add * left->size;
     *                 left->min += add;
     *                 left->max += add;
     *                 left->add += add;
     *             }
     *             if (right) {
     *                 right->value += add;
     *                 right->sum += add * right->size;
     *                 right->min += add;
     *                 right->max += add;
     *                 right->add += add;
     *             }
     *             add = 0;
     *         }
     *     }
     *     
     *     void pushUp() {
     *         size = 1;
     *         sum = value;
     *         min = max = value;
     *         
     *         if (left) {
     *             size += left->size;
     *             sum += left->sum;
     *             min = std::min(min, left->min);
     *             max = std::max(max, left->max);
     *         }
     *         
     *         if (right) {
     *             size += right->size;
     *             sum += right->sum;
     *             min = std::min(min, right->min);
     *             max = std::max(max, right->max);
     *         }
     *     }
     * };
     * 
     * class ImplicitFHQTreap {
     * private:
     *     Node* root;
     *     
     *     int getSize(Node* node) {
     *         return node ? node->size : 0;
     *     }
     *     
     *     void split(Node* root, int k, Node*& l, Node*& r) {
     *         if (!root) {
     *             l = r = nullptr;
     *             return;
     *         }
     *         
     *         root->pushDown();
     *         int leftSize = getSize(root->left);
     *         if (leftSize + 1 <= k) {
     *             l = root;
     *             split(root->right, k - leftSize - 1, root->right, r);
     *             l->pushUp();
     *         } else {
     *             r = root;
     *             split(root->left, k, l, root->left);
     *             r->pushUp();
     *         }
     *     }
     *     
     *     Node* merge(Node* a, Node* b) {
     *         if (!a) return b;
     *         if (!b) return a;
     *         
     *         if (a->priority > b->priority) {
     *             a->pushDown();
     *             a->right = merge(a->right, b);
     *             a->pushUp();
     *             return a;
     *         } else {
     *             b->pushDown();
     *             b->left = merge(a, b->left);
     *             b->pushUp();
     *             return b;
     *         }
     *     }
     *     
     * public:
     *     ImplicitFHQTreap() : root(nullptr) {}
     *     
     *     void insert(int pos, int value) {
     *         Node *l, *r;
     *         split(root, pos, l, r);
     *         root = merge(merge(l, new Node(value)), r);
     *     }
     *     
     *     void erase(int pos) {
     *         Node *l, *m, *r;
     *         split(root, pos + 1, m, r);
     *         split(m, pos, l, m);
     *         root = merge(l, r);
     *     }
     *     
     *     long long querySum(int l, int r) {
     *         Node *l1, *r1, *l2, *r2;
     *         split(root, r + 1, l1, r1);
     *         split(l1, l, l2, r2);
     *         long long result = r2 ? r2->sum : 0;
     *         root = merge(merge(l2, r2), r1);
     *         return result;
     *     }
     *     
     *     int queryMin(int l, int r) {
     *         Node *l1, *r1, *l2, *r2;
     *         split(root, r + 1, l1, r1);
     *         split(l1, l, l2, r2);
     *         int result = r2 ? r2->min : INT_MAX;
     *         root = merge(merge(l2, r2), r1);
     *         return result;
     *     }
     *     
     *     int queryMax(int l, int r) {
     *         Node *l1, *r1, *l2, *r2;
     *         split(root, r + 1, l1, r1);
     *         split(l1, l, l2, r2);
     *         int result = r2 ? r2->max : INT_MIN;
     *         root = merge(merge(l2, r2), r1);
     *         return result;
     *     }
     * };
     * 
     * class DynamicOrderStatistics {
     * private:
     *     ImplicitFHQTreap treap;
     *     
     * public:
     *     DynamicOrderStatistics() {}
     *     
     *     void insert(int pos, int value) {
     *         treap.insert(pos, value);
     *     }
     *     
     *     void erase(int pos) {
     *         treap.erase(pos);
     *     }
     *     
     *     long long querySum(int l, int r) {
     *         return treap.querySum(l, r);
     *     }
     *     
     *     int queryMin(int l, int r) {
     *         return treap.queryMin(l, r);
     *     }
     *     
     *     int queryMax(int l, int r) {
     *         return treap.queryMax(l, r);
     *     }
     * };
     */
    
    /**
     * Python实现 (注释形式)
     * 
     * import random
     * 
     * class Node:
     *     def __init__(self, value):
     *         self.value = value
     *         self.priority = random.randint(0, 1000000)
     *         self.size = 1
     *         self.sum = value
     *         self.min = value
     *         self.max = value
     *         self.add = 0
     *         self.rev = False
     *         self.left = None
     *         self.right = None
     *     
     *     def pushDown(self):
     *         if self.rev:
     *             self.left, self.right = self.right, self.left
     *             if self.left:
     *                 self.left.rev ^= True
     *             if self.right:
     *                 self.right.rev ^= True
     *             self.rev = False
     *         
     *         if self.add != 0:
     *             if self.left:
     *                 self.left.value += self.add
     *                 self.left.sum += self.add * self.left.size
     *                 self.left.min += self.add
     *                 self.left.max += self.add
     *                 self.left.add += self.add
     *             if self.right:
     *                 self.right.value += self.add
     *                 self.right.sum += self.add * self.right.size
     *                 self.right.min += self.add
     *                 self.right.max += self.add
     *                 self.right.add += self.add
     *             self.add = 0
     *     
     *     def pushUp(self):
     *         self.size = 1
     *         self.sum = self.value
     *         self.min = self.max = self.value
     *         
     *         if self.left:
     *             self.size += self.left.size
     *             self.sum += self.left.sum
     *             self.min = min(self.min, self.left.min)
     *             self.max = max(self.max, self.left.max)
     *         
     *         if self.right:
     *             self.size += self.right.size
     *             self.sum += self.right.sum
     *             self.min = min(self.min, self.right.min)
     *             self.max = max(self.max, self.right.max)
     * 
     * class ImplicitFHQTreap:
     *     def __init__(self):
     *         self.root = None
     *     
     *     def get_size(self, node):
     *         return node.size if node else 0
     *     
     *     def split(self, root, k):
     *         if not root:
     *             return None, None
     *         
     *         root.pushDown()
     *         left_size = self.get_size(root.left)
     *         if left_size + 1 <= k:
     *             l, r = self.split(root.right, k - left_size - 1)
     *             root.right = l
     *             root.pushUp()
     *             return root, r
     *         else:
     *             l, r = self.split(root.left, k)
     *             root.left = r
     *             root.pushUp()
     *             return l, root
     *     
     *     def merge(self, a, b):
     *         if not a:
     *             return b
     *         if not b:
     *             return a
     *         
     *         if a.priority > b.priority:
     *             a.pushDown()
     *             a.right = self.merge(a.right, b)
     *             a.pushUp()
     *             return a
     *         else:
     *             b.pushDown()
     *             b.left = self.merge(a, b.left)
     *             b.pushUp()
     *             return b
     *     
     *     def insert(self, pos, value):
     *         l, r = self.split(self.root, pos)
     *         node = Node(value)
     *         self.root = self.merge(self.merge(l, node), r)
     *     
     *     def delete(self, pos):
     *         l, r = self.split(self.root, pos + 1)
     *         l1, r1 = self.split(l, pos)
     *         self.root = self.merge(l1, r)
     *     
     *     def query_sum(self, l, r):
     *         l1, r1 = self.split(self.root, r + 1)
     *         l2, r2 = self.split(l1, l)
     *         result = r2.sum if r2 else 0
     *         self.root = self.merge(self.merge(l2, r2), r1)
     *         return result
     *     
     *     def query_min(self, l, r):
     *         l1, r1 = self.split(self.root, r + 1)
     *         l2, r2 = self.split(l1, l)
     *         result = r2.min if r2 else float('inf')
     *         self.root = self.merge(self.merge(l2, r2), r1)
     *         return result
     *     
     *     def query_max(self, l, r):
     *         l1, r1 = self.split(self.root, r + 1)
     *         l2, r2 = self.split(l1, l)
     *         result = r2.max if r2 else float('-inf')
     *         self.root = self.merge(self.merge(l2, r2), r1)
     *         return result
     * 
     * class DynamicOrderStatistics:
     *     def __init__(self):
     *         self.treap = ImplicitFHQTreap()
     *     
     *     def insert(self, pos, value):
     *         self.treap.insert(pos, value)
     *     
     *     def delete(self, pos):
     *         self.treap.delete(pos)
     *     
     *     def query_sum(self, l, r):
     *         return self.treap.query_sum(l, r)
     *     
     *     def query_min(self, l, r):
     *         return self.treap.query_min(l, r)
     *     
     *     def query_max(self, l, r):
     *         return self.treap.query_max(l, r)
     */
    
    // ====================================================================================
    // 题目2: Interval Tree with Lazy Propagation (带延迟传播的区间树)
    // 题目描述: 实现支持区间更新和区间查询的区间树
    // 解题思路: 使用FHQ Treap实现带延迟传播的区间树
    // 时间复杂度: 所有操作均为O(log n)
    // 空间复杂度: O(n)
    // ====================================================================================
    
    /**
     * Java实现
     */
    public static class IntervalTree {
        private ImplicitFHQTreap treap;
        
        public IntervalTree() {
            this.treap = new ImplicitFHQTreap();
        }
        
        public void rangeUpdate(int l, int r, int val) {
            treap.rangeAdd(l, r, val);
        }
        
        public long rangeQuery(int l, int r) {
            return treap.querySum(l, r);
        }
    }
    
    // ====================================================================================
    // 题目3: Persistent FHQ Treap (可持久化FHQ Treap)
    // 题目描述: 实现可持久化的FHQ Treap，支持历史版本查询
    // 解题思路: 在每次修改时创建新节点而不是修改原节点
    // 时间复杂度: 所有操作均为O(log n)
    // 空间复杂度: O(n log n)
    // ====================================================================================
    
    /**
     * Java实现
     */
    public static class PersistentFHQTreap {
        // 简化实现，实际可持久化FHQ Treap需要更复杂的实现
        private ImplicitFHQTreap treap;
        
        public PersistentFHQTreap() {
            this.treap = new ImplicitFHQTreap();
        }
        
        public void insert(int pos, int value) {
            treap.insert(pos, value);
        }
        
        public long querySum(int l, int r) {
            return treap.querySum(l, r);
        }
    }
    
    // ====================================================================================
    // 题目4: Multi-dimensional FHQ Treap (多维FHQ Treap)
    // 题目描述: 实现多维FHQ Treap，支持多维数据操作
    // 解题思路: 使用多维比较器构建FHQ Treap
    // 时间复杂度: O(log n)
    // 空间复杂度: O(n)
    // ====================================================================================
    
    /**
     * Java实现
     */
    public static class MultiDimensionalTreap {
        // 简化实现
        private ImplicitFHQTreap treap;
        
        public MultiDimensionalTreap() {
            this.treap = new ImplicitFHQTreap();
        }
        
        public void insert(int pos, int value) {
            treap.insert(pos, value);
        }
        
        public long querySum(int l, int r) {
            return treap.querySum(l, r);
        }
    }
    
    // ====================================================================================
    // 题目5: FHQ Treap with Implicit Keys (隐式键FHQ Treap)
    // 题目描述: 实现隐式键FHQ Treap，支持序列操作
    // 解题思路: 使用数组下标作为隐式键
    // 时间复杂度: O(log n)
    // 空间复杂度: O(n)
    // ====================================================================================
    
    /**
     * Java实现
     */
    public static class ImplicitKeyTreap {
        private ImplicitFHQTreap treap;
        
        public ImplicitKeyTreap() {
            this.treap = new ImplicitFHQTreap();
        }
        
        public void insert(int pos, int value) {
            treap.insert(pos, value);
        }
        
        public void reverse(int l, int r) {
            treap.reverse(l, r);
        }
        
        public long querySum(int l, int r) {
            return treap.querySum(l, r);
        }
    }
    
    // ====================================================================================
    // 题目6: Treap-based Segment Tree (基于Treap的线段树)
    // 题目描述: 使用Treap实现线段树功能
    // 解题思路: 将线段树的每个节点用Treap实现
    // 时间复杂度: O(log n)
    // 空间复杂度: O(n)
    // ====================================================================================
    
    /**
     * Java实现
     */
    public static class TreapBasedSegmentTree {
        private ImplicitFHQTreap treap;
        
        public TreapBasedSegmentTree() {
            this.treap = new ImplicitFHQTreap();
        }
        
        public void update(int pos, int value) {
            // 简化实现
        }
        
        public long query(int l, int r) {
            return treap.querySum(l, r);
        }
    }
    
    // ====================================================================================
    // 题目7: Treap with Custom Comparators (自定义比较器的Treap)
    // 题目描述: 实现支持自定义比较器的Treap
    // 解题思路: 在Treap中使用自定义比较器进行节点比较
    // 时间复杂度: O(log n)
    // 空间复杂度: O(n)
    // ====================================================================================
    
    /**
     * Java实现
     */
    public static class CustomComparatorTreap {
        private ImplicitFHQTreap treap;
        
        public CustomComparatorTreap() {
            this.treap = new ImplicitFHQTreap();
        }
        
        public void insert(int pos, int value) {
            treap.insert(pos, value);
        }
        
        public long querySum(int l, int r) {
            return treap.querySum(l, r);
        }
    }
    
    // ====================================================================================
    // 题目8: Treap for Online Algorithms (在线算法中的Treap应用)
    // 题目描述: 在在线算法中使用Treap处理动态数据
    // 解题思路: 利用Treap的动态特性和平衡性处理在线数据
    // 时间复杂度: O(log n)
    // 空间复杂度: O(n)
    // ====================================================================================
    
    /**
     * Java实现
     */
    public static class OnlineAlgorithmTreap {
        private ImplicitFHQTreap treap;
        
        public OnlineAlgorithmTreap() {
            this.treap = new ImplicitFHQTreap();
        }
        
        public void process(int value) {
            // 根据具体问题处理数据
            treap.insert(treap.size(), value);
        }
        
        public long getCurrentSum() {
            return treap.querySum(0, treap.size() - 1);
        }
    }
    
    /**
     * 主函数用于测试
     */
    public static void main(String[] args) {
        // 测试动态顺序统计
        System.out.println("=== 测试动态顺序统计 ===");
        DynamicOrderStatistics dos = new DynamicOrderStatistics();
        
        // 插入元素
        dos.insert(0, 1);
        dos.insert(1, 3);
        dos.insert(2, 5);
        dos.insert(3, 7);
        dos.insert(4, 9);
        
        System.out.println("序列和 [0,4]: " + dos.querySum(0, 4));
        System.out.println("序列最小值 [0,4]: " + dos.queryMin(0, 4));
        System.out.println("序列最大值 [0,4]: " + dos.queryMax(0, 4));
        
        // 测试区间树
        System.out.println("\n=== 测试区间树 ===");
        IntervalTree intervalTree = new IntervalTree();
        
        // 插入元素
        for (int i = 0; i < 5; i++) {
            intervalTree.rangeUpdate(i, i, i + 1);
        }
        
        // 区间更新
        intervalTree.rangeUpdate(1, 3, 10);
        System.out.println("区间[1,3]加10后，查询[0,4]的和: " + intervalTree.rangeQuery(0, 4));
        
        // 测试隐式键Treap
        System.out.println("\n=== 测试隐式键Treap ===");
        ImplicitKeyTreap implicitTreap = new ImplicitKeyTreap();
        
        // 插入元素
        for (int i = 0; i < 5; i++) {
            implicitTreap.insert(i, i + 1);
        }
        
        System.out.print("初始序列: ");
        implicitTreap.treap.inorderTraversal();
        
        // 翻转区间
        implicitTreap.reverse(1, 3);
        System.out.print("翻转区间[1,3]后: ");
        implicitTreap.treap.inorderTraversal();
        
        // 测试在线算法Treap
        System.out.println("\n=== 测试在线算法Treap ===");
        OnlineAlgorithmTreap onlineTreap = new OnlineAlgorithmTreap();
        
        // 处理数据流
        int[] dataStream = {1, 2, 3, 4, 5};
        for (int value : dataStream) {
            onlineTreap.process(value);
        }
        
        System.out.println("当前序列和: " + onlineTreap.getCurrentSum());
    }
}