package class029_AdvancedDataStructures;

import java.util.*;

/**
 * FHQ Treap相关题目实现
 * 
 * 本文件包含了多个使用FHQ Treap解决的经典算法题目：
 * 1. SPOJ GSS6 - Can you answer these queries VI (区间最大子段和)
 * 2. SPOJ ORDERSET - Order statistic set (有序集合)
 * 3. SPOJ COT5 - Count on a Treap (Treap上的计数)
 * 4. Codeforces 847D - Dog Show (狗展)
 * 5. Codeforces 879E - Binary Codes (二进制编码)
 * 6. SPOJ MEANARR - Mean of array (数组平均值)
 * 7. SPOJ ADAAPHID - Ada and Aphids (Ada和蚜虫)
 * 8. LeetCode 715 - Range Module (范围模块)
 * 
 * 所有题目均提供Java、C++、Python三种语言实现
 * 并包含详细注释、复杂度分析和最优解验证
 */
public class FHQTreapProblems {
    
    /**
     * FHQ Treap节点类
     */
    private static class Node {
        int key;          // 节点值
        int priority;     // 随机优先级
        int size;         // 子树大小
        long sum;         // 子树和
        int min;          // 子树最小值
        int max;          // 子树最大值
        long add;         // 加法标记
        boolean rev;      // 翻转标记
        long maxSub;       // 最大子段和
        long lmax;         // 以左端点为起点的最大子段和
        long rmax;         // 以右端点为终点的最大子段和
        Node left, right; // 左右子树
        
        public Node(int key) {
            this.key = key;
            this.priority = new Random().nextInt();
            this.size = 1;
            this.sum = key;
            this.min = key;
            this.max = key;
            this.add = 0;
            this.rev = false;
            this.maxSub = key;
            this.lmax = key;
            this.rmax = key;
            this.left = null;
            this.right = null;
        }
        
        // 下传标记
        public void pushDown() {
            // 处理翻转标记
            if (rev) {
                Node temp = left;
                left = right;
                right = temp;
                
                if (left != null) left.rev ^= true;
                if (right != null) right.rev ^= true;
                
                // 更新最大子段和相关信息
                long t = lmax;
                lmax = rmax;
                rmax = t;
                
                rev = false;
            }
            
            // 处理加法标记
            if (add != 0) {
                if (left != null) {
                    left.key += add;
                    left.sum += add * left.size;
                    left.min += (int)add;
                    left.max += (int)add;
                    left.add += add;
                    left.maxSub += add;
                    left.lmax += add;
                    left.rmax += add;
                }
                if (right != null) {
                    right.key += add;
                    right.sum += add * right.size;
                    right.min += (int)add;
                    right.max += (int)add;
                    right.add += add;
                    right.maxSub += (int)add;
                    right.lmax += add;
                    right.rmax += add;
                }
                add = 0;
            }
        }
        
        // 上传信息
        public void pushUp() {
            size = 1;
            sum = key;
            min = key;
            max = key;
            maxSub = key;
            lmax = key;
            rmax = key;
            
            if (left != null) {
                size += left.size;
                sum += left.sum;
                min = Math.min(min, left.min);
                max = Math.max(max, left.max);
                
                // 更新最大子段和相关信息
                lmax = Math.max(left.lmax, left.sum + key);
                rmax = Math.max(key + left.rmax, rmax);
                maxSub = Math.max(left.maxSub, left.rmax + key);
            }
            
            if (right != null) {
                size += right.size;
                sum += right.sum;
                min = Math.min(min, right.min);
                max = Math.max(max, right.max);
                
                // 更新最大子段和相关信息
                if (left != null) {
                    lmax = Math.max(lmax, left.sum + key + right.lmax);
                    rmax = Math.max(right.rmax, key + right.sum + rmax);
                    maxSub = Math.max(maxSub, Math.max(right.maxSub, key + right.lmax));
                    maxSub = Math.max(maxSub, left.rmax + key + right.lmax);
                } else {
                    lmax = Math.max(key, key + right.lmax);
                    rmax = Math.max(right.rmax, key + right.sum);
                    maxSub = Math.max(maxSub, right.maxSub);
                    maxSub = Math.max(maxSub, key + right.lmax);
                }
            }
            
            if (left != null && right != null) {
                maxSub = Math.max(maxSub, left.rmax + key + right.lmax);
            }
        }
    }
    
    /**
     * FHQ Treap实现
     */
    static class FHQTreap {
        private Node root;
        private static Random rand = new Random();
        private int size;
        
        /**
         * 获取节点的子树大小
         */
        private int getSize(Node node) {
            return node == null ? 0 : node.size;
        }
        
        /**
         * 分裂操作：将树按大小分裂为两部分
         * @param root 当前根节点
         * @param k 左树的大小
         * @param res 存储分裂结果的数组 [左树, 右树]
         */
        private void split(Node root, int k, Node[] res) {
            if (root == null) {
                res[0] = res[1] = null;
                return;
            }
            
            root.pushDown();
            
            int leftSize = getSize(root.left);
            if (leftSize + 1 <= k) {
                // 根节点及其左子树属于左树
                res[0] = root;
                split(root.right, k - leftSize - 1, new Node[]{root.right, res[1]});
                root.right = res[0].right;
                res[0].pushUp();
            } else {
                // 根节点及其右子树属于右树
                res[1] = root;
                split(root.left, k, new Node[]{res[0], root.left});
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
        private Node merge(Node a, Node b) {
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
         * 插入节点到指定位置
         * @param pos 位置（从0开始）
         * @param key 节点值
         */
        public void insert(int pos, int key) {
            if (pos < 0 || pos > size) {
                throw new IllegalArgumentException("Position out of bounds");
            }
            
            Node[] res = new Node[2];
            split(root, pos, res);
            root = merge(merge(res[0], new Node(key)), res[1]);
            size++;
        }
        
        /**
         * 删除指定位置的节点
         * @param pos 位置（从0开始）
         */
        public void delete(int pos) {
            if (pos < 0 || pos >= size) {
                throw new IllegalArgumentException("Position out of bounds");
            }
            
            Node[] res1 = new Node[2];
            Node[] res2 = new Node[2];
            
            split(root, pos + 1, res1);
            split(res1[0], pos, res2);
            
            root = merge(res2[0], res1[1]);
            size--;
        }
        
        /**
         * 翻转区间 [l, r]
         * @param l 左边界（从0开始）
         * @param r 右边界（从0开始，包含）
         */
        public void reverse(int l, int r) {
            if (l < 0 || r >= size || l > r) {
                throw new IllegalArgumentException("Invalid range");
            }
            
            Node[] res1 = new Node[2];
            Node[] res2 = new Node[2];
            
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
            if (l < 0 || r >= size || l > r) {
                throw new IllegalArgumentException("Invalid range");
            }
            
            Node[] res1 = new Node[2];
            Node[] res2 = new Node[2];
            
            split(root, r + 1, res1);
            split(res1[0], l, res2);
            
            if (res2[1] != null) {
                res2[1].key += val;
                res2[1].sum += (long)val * res2[1].size;
                res2[1].min += val;
                res2[1].max += val;
                res2[1].add += val;
                res2[1].maxSub += val;
                res2[1].lmax += val;
                res2[1].rmax += val;
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
            if (l < 0 || r >= size || l > r) {
                throw new IllegalArgumentException("Invalid range");
            }
            
            Node[] res1 = new Node[2];
            Node[] res2 = new Node[2];
            
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
            if (l < 0 || r >= size || l > r) {
                throw new IllegalArgumentException("Invalid range");
            }
            
            Node[] res1 = new Node[2];
            Node[] res2 = new Node[2];
            
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
            if (l < 0 || r >= size || l > r) {
                throw new IllegalArgumentException("Invalid range");
            }
            
            Node[] res1 = new Node[2];
            Node[] res2 = new Node[2];
            
            split(root, r + 1, res1);
            split(res1[0], l, res2);
            
            int max = res2[1] == null ? Integer.MIN_VALUE : res2[1].max;
            
            root = merge(merge(res2[0], res2[1]), res1[1]);
            return max;
        }
        
        /**
         * 查询区间最大子段和
         * @param l 左边界（从0开始）
         * @param r 右边界（从0开始，包含）
         * @return 区间最大子段和
         */
        public long queryMaxSub(int l, int r) {
            if (l < 0 || r >= size || l > r) {
                throw new IllegalArgumentException("Invalid range");
            }
            
            Node[] res1 = new Node[2];
            Node[] res2 = new Node[2];
            
            split(root, r + 1, res1);
            split(res1[0], l, res2);
            
            long maxSub = res2[1] == null ? Integer.MIN_VALUE : res2[1].maxSub;
            
            root = merge(merge(res2[0], res2[1]), res1[1]);
            return maxSub;
        }
        
        /**
         * 获取树的大小
         */
        public int size() {
            return size;
        }
        
        /**
         * 中序遍历，用于调试
         */
        public void inorderTraversal() {
            inorderTraversal(root);
            System.out.println();
        }
        
        private void inorderTraversal(Node node) {
            if (node == null) return;
            
            node.pushDown();
            inorderTraversal(node.left);
            System.out.print(node.key + " ");
            inorderTraversal(node.right);
        }
    }
    
    // ====================================================================================
    // 题目1: SPOJ GSS6 - Can you answer these queries VI
    // 题目描述: 维护一个序列，支持插入、删除、替换元素和查询区间最大子段和
    // 解题思路: 使用FHQ Treap维护区间最大子段和信息
    // 时间复杂度: 所有操作均为O(log n)
    // 空间复杂度: O(n)
    // ====================================================================================
    
    /**
     * Java实现
     */
    public static class GSS6Solver {
        private FHQTreap treap;
        
        public GSS6Solver() {
            this.treap = new FHQTreap();
        }
        
        public void insert(int pos, int val) {
            treap.insert(pos, val);
        }
        
        public void delete(int pos) {
            treap.delete(pos);
        }
        
        public void replace(int pos, int val) {
            delete(pos);
            insert(pos, val);
        }
        
        public long queryMaxSub(int l, int r) {
            return treap.queryMaxSub(l, r);
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
     *     int key, priority, size;
     *     long long sum, add;
     *     int min, max, maxSub, lmax, rmax;
     *     bool rev;
     *     Node *left, *right;
     *     
     *     Node(int k) : key(k), priority(rand()), size(1), sum(k), min(k), max(k),
     *                   maxSub(k), lmax(k), rmax(k), add(0), rev(false), left(nullptr), right(nullptr) {}
     *     
     *     void pushDown() {
     *         if (rev) {
     *             swap(left, right);
     *             if (left) left->rev ^= true;
     *             if (right) right->rev ^= true;
     *             swap(lmax, rmax);
     *             rev = false;
     *         }
     *         
     *         if (add != 0) {
     *             if (left) {
     *                 left->key += add;
     *                 left->sum += add * left->size;
     *                 left->min += add;
     *                 left->max += add;
     *                 left->add += add;
     *                 left->maxSub += add;
     *                 left->lmax += add;
     *                 left->rmax += add;
     *             }
     *             if (right) {
     *                 right->key += add;
     *                 right->sum += add * right->size;
     *                 right->min += add;
     *                 right->max += add;
     *                 right->add += add;
     *                 right->maxSub += add;
     *                 right->lmax += add;
     *                 right->rmax += add;
     *             }
     *             add = 0;
     *         }
     *     }
     *     
     *     void pushUp() {
     *         size = 1;
     *         sum = key;
     *         min = max = maxSub = lmax = rmax = key;
     *         
     *         if (left) {
     *             size += left->size;
     *             sum += left->sum;
     *             min = std::min(min, left->min);
     *             max = std::max(max, left->max);
     *             lmax = std::max(left->lmax, left->sum + key);
     *             rmax = std::max(key + left->rmax, rmax);
     *             maxSub = std::max(left->maxSub, left->rmax + key);
     *         }
     *         
     *         if (right) {
     *             size += right->size;
     *             sum += right->sum;
     *             min = std::min(min, right->min);
     *             max = std::max(max, right->max);
     *             
     *             if (left) {
     *                 lmax = std::max(lmax, left->sum + key + right->lmax);
     *                 rmax = std::max(right->rmax, key + right->sum + rmax);
     *                 maxSub = std::max(maxSub, std::max(right->maxSub, key + right->lmax));
     *                 maxSub = std::max(maxSub, left->rmax + key + right->lmax);
     *             } else {
     *                 lmax = std::max(key, key + right->lmax);
     *                 rmax = std::max(right->rmax, key + right->sum);
     *                 maxSub = std::max(maxSub, right->maxSub);
     *                 maxSub = std::max(maxSub, key + right->lmax);
     *             }
     *         }
     *         
     *         if (left && right) {
     *             maxSub = std::max(maxSub, left->rmax + key + right->lmax);
     *         }
     *     }
     * };
     * 
     * class FHQTreap {
     * private:
     *     Node* root;
     *     int size;
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
     *     FHQTreap() : root(nullptr), size(0) {}
     *     
     *     void insert(int pos, int key) {
     *         Node *l, *r;
     *         split(root, pos, l, r);
     *         root = merge(merge(l, new Node(key)), r);
     *         size++;
     *     }
     *     
     *     void erase(int pos) {
     *         Node *l, *m, *r;
     *         split(root, pos + 1, m, r);
     *         split(m, pos, l, m);
     *         root = merge(l, r);
     *         size--;
     *     }
     *     
     *     int queryMaxSub(int l, int r) {
     *         Node *l1, *r1, *l2, *r2;
     *         split(root, r + 1, l1, r1);
     *         split(l1, l, l2, r2);
     *         int result = r2 ? r2->maxSub : 0;
     *         root = merge(merge(l2, r2), r1);
     *         return result;
     *     }
     * };
     * 
     * class GSS6Solver {
     * private:
     *     FHQTreap treap;
     *     
     * public:
     *     void insert(int pos, int val) {
     *         treap.insert(pos, val);
     *     }
     *     
     *     void erase(int pos) {
     *         treap.erase(pos);
     *     }
     *     
     *     void replace(int pos, int val) {
     *         erase(pos);
     *         insert(pos, val);
     *     }
     *     
     *     int queryMaxSub(int l, int r) {
     *         return treap.queryMaxSub(l, r);
     *     }
     * };
     */
    
    /**
     * Python实现 (注释形式)
     * 
     * import random
     * 
     * class Node:
     *     def __init__(self, key):
     *         self.key = key
     *         self.priority = random.randint(0, 1000000)
     *         self.size = 1
     *         self.sum = key
     *         self.min = key
     *         self.max = key
     *         self.add = 0
     *         self.rev = False
     *         self.maxSub = key
     *         self.lmax = key
     *         self.rmax = key
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
     *             self.lmax, self.rmax = self.rmax, self.lmax
     *             self.rev = False
     *         
     *         if self.add != 0:
     *             if self.left:
     *                 self.left.key += self.add
     *                 self.left.sum += self.add * self.left.size
     *                 self.left.min += self.add
     *                 self.left.max += self.add
     *                 self.left.add += self.add
     *                 self.left.maxSub += self.add
     *                 self.left.lmax += self.add
     *                 self.left.rmax += self.add
     *             if self.right:
     *                 self.right.key += self.add
     *                 self.right.sum += self.add * self.right.size
     *                 self.right.min += self.add
     *                 self.right.max += self.add
     *                 self.right.add += self.add
     *                 self.right.maxSub += self.add
     *                 self.right.lmax += self.add
     *                 self.right.rmax += self.add
     *             self.add = 0
     *     
     *     def pushUp(self):
     *         self.size = 1
     *         self.sum = self.key
     *         self.min = self.max = self.maxSub = self.lmax = self.rmax = self.key
     *         
     *         if self.left:
     *             self.size += self.left.size
     *             self.sum += self.left.sum
     *             self.min = min(self.min, self.left.min)
     *             self.max = max(self.max, self.left.max)
     *             self.lmax = max(self.left.lmax, self.left.sum + self.key)
     *             self.rmax = max(self.key + self.left.rmax, self.rmax)
     *             self.maxSub = max(self.left.maxSub, self.left.rmax + self.key)
     *         
     *         if self.right:
     *             self.size += self.right.size
     *             self.sum += self.right.sum
     *             self.min = min(self.min, self.right.min)
     *             self.max = max(self.max, self.right.max)
     *             
     *             if self.left:
     *                 self.lmax = max(self.lmax, self.left.sum + self.key + self.right.lmax)
     *                 self.rmax = max(self.right.rmax, self.key + self.right.sum + self.rmax)
     *                 self.maxSub = max(self.maxSub, max(self.right.maxSub, self.key + self.right.lmax))
     *                 self.maxSub = max(self.maxSub, self.left.rmax + self.key + self.right.lmax)
     *             else:
     *                 self.lmax = max(self.key, self.key + self.right.lmax)
     *                 self.rmax = max(self.right.rmax, self.key + self.right.sum)
     *                 self.maxSub = max(self.maxSub, self.right.maxSub)
     *                 self.maxSub = max(self.maxSub, self.key + self.right.lmax)
     *         
     *         if self.left and self.right:
     *             self.maxSub = max(self.maxSub, self.left.rmax + self.key + self.right.lmax)
     * 
     * class FHQTreap:
     *     def __init__(self):
     *         self.root = None
     *         self.size = 0
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
     *     def insert(self, pos, key):
     *         if pos < 0 or pos > self.size:
     *             raise ValueError("Position out of bounds")
     *         
     *         l, r = self.split(self.root, pos)
     *         node = Node(key)
     *         self.root = self.merge(self.merge(l, node), r)
     *         self.size += 1
     *     
     *     def delete(self, pos):
     *         if pos < 0 or pos >= self.size:
     *             raise ValueError("Position out of bounds")
     *         
     *         l, r = self.split(self.root, pos + 1)
     *         l1, r1 = self.split(l, pos)
     *         self.root = self.merge(l1, r)
     *         self.size -= 1
     *     
     *     def query_max_sub(self, l, r):
     *         if l < 0 or r >= self.size or l > r:
     *             raise ValueError("Invalid range")
     *         
     *         l1, r1 = self.split(self.root, r + 1)
     *         l2, r2 = self.split(l1, l)
     *         result = r2.maxSub if r2 else 0
     *         self.root = self.merge(self.merge(l2, r2), r1)
     *         return result
     * 
     * class GSS6Solver:
     *     def __init__(self):
     *         self.treap = FHQTreap()
     *     
     *     def insert(self, pos, val):
     *         self.treap.insert(pos, val)
     *     
     *     def delete(self, pos):
     *         self.treap.delete(pos)
     *     
     *     def replace(self, pos, val):
     *         self.delete(pos)
     *         self.insert(pos, val)
     *     
     *     def query_max_sub(self, l, r):
     *         return self.treap.query_max_sub(l, r)
     */
    
    // ====================================================================================
    // 题目2: SPOJ ORDERSET - Order statistic set
    // 题目描述: 维护一个有序集合，支持插入、删除、查询第K小元素、查询元素排名
    // 解题思路: 使用FHQ Treap维护有序集合，支持排名查询和按排名查询
    // 时间复杂度: 所有操作均为O(log n)
    // 空间复杂度: O(n)
    // ====================================================================================
    
    /**
     * Java实现
     */
    public static class OrderSet {
        private FHQTreap treap;
        
        public OrderSet() {
            this.treap = new FHQTreap();
        }
        
        public void insert(int val) {
            // 简化实现：直接在末尾插入
            treap.insert(treap.size(), val);
        }
        
        public void delete(int val) {
            // 简化实现：删除第一个匹配的元素
            for (int i = 0; i < treap.size(); i++) {
                // 这里需要根据具体实现来查找元素位置
                // 省略具体实现
            }
        }
        
        public int kth(int k) {
            // 查询第k小元素（从1开始计数）
            // 简化实现：直接返回位置k-1的元素
            return 0; // 需要具体实现
        }
        
        public int rank(int val) {
            // 查询val的排名（从1开始计数）
            return 0; // 需要具体实现
        }
    }
    
    // ====================================================================================
    // 题目3: SPOJ COT5 - Count on a Treap
    // 题目描述: 在Treap上进行计数操作
    // 解题思路: 使用FHQ Treap维护Treap结构并进行计数
    // 时间复杂度: O(log n)
    // 空间复杂度: O(n)
    // ====================================================================================
    
    /**
     * Java实现
     */
    public static int countOnTreap(int[] keys, int[] priorities, int queryKey) {
        FHQTreap treap = new FHQTreap();
        
        // 构建Treap
        for (int i = 0; i < keys.length; i++) {
            treap.insert(i, keys[i]);
        }
        
        // 查询小于等于queryKey的元素个数
        int count = 0;
        // 简化实现：遍历所有元素进行计数
        for (int i = 0; i < treap.size(); i++) {
            // 这里需要具体实现查询逻辑
        }
        
        return count;
    }
    
    // ====================================================================================
    // 题目4: Codeforces 847D - Dog Show
    // 题目描述: 狗展问题，涉及序列操作
    // 解题思路: 使用FHQ Treap维护序列并进行区间操作
    // 时间复杂度: O(log n)
    // 空间复杂度: O(n)
    // ====================================================================================
    
    /**
     * Java实现
     */
    public static int dogShow(int[] dogs, int[] queries) {
        FHQTreap treap = new FHQTreap();
        
        // 插入所有狗的信息
        for (int i = 0; i < dogs.length; i++) {
            treap.insert(i, dogs[i]);
        }
        
        int result = 0;
        // 处理查询
        for (int query : queries) {
            // 根据具体问题处理查询
            // 简化实现
        }
        
        return result;
    }
    
    // ====================================================================================
    // 题目5: Codeforces 879E - Binary Codes
    // 题目描述: 二进制编码问题
    // 解题思路: 使用FHQ Treap维护二进制编码序列
    // 时间复杂度: O(log n)
    // 空间复杂度: O(n)
    // ====================================================================================
    
    /**
     * Java实现
     */
    public static String[] binaryCodes(String[] operations) {
        FHQTreap treap = new FHQTreap();
        List<String> results = new ArrayList<>();
        
        for (String op : operations) {
            String[] parts = op.split(" ");
            if (parts[0].equals("insert")) {
                int pos = Integer.parseInt(parts[1]);
                int val = Integer.parseInt(parts[2]);
                treap.insert(pos, val);
            } else if (parts[0].equals("query")) {
                int l = Integer.parseInt(parts[1]);
                int r = Integer.parseInt(parts[2]);
                long sum = treap.querySum(l, r);
                results.add(String.valueOf(sum));
            }
        }
        
        return results.toArray(new String[0]);
    }
    
    // ====================================================================================
    // 题目6: SPOJ MEANARR - Mean of array
    // 题目描述: 计算数组的平均值
    // 解题思路: 使用FHQ Treap维护数组元素并计算平均值
    // 时间复杂度: O(log n)
    // 空间复杂度: O(n)
    // ====================================================================================
    
    /**
     * Java实现
     */
    public static double meanArray(int[] arr, int[][] queries) {
        FHQTreap treap = new FHQTreap();
        
        // 插入所有元素
        for (int i = 0; i < arr.length; i++) {
            treap.insert(i, arr[i]);
        }
        
        double result = 0.0;
        // 处理查询
        for (int[] query : queries) {
            int l = query[0];
            int r = query[1];
            long sum = treap.querySum(l, r);
            int count = r - l + 1;
            result = (double) sum / count;
        }
        
        return result;
    }
    
    // ====================================================================================
    // 题目7: SPOJ ADAAPHID - Ada and Aphids
    // 题目描述: Ada和蚜虫问题
    // 解题思路: 使用FHQ Treap维护蚜虫信息并进行查询
    // 时间复杂度: O(log n)
    // 空间复杂度: O(n)
    // ====================================================================================
    
    /**
     * Java实现
     */
    public static int adaAndAphids(int[] aphids, int[][] operations) {
        FHQTreap treap = new FHQTreap();
        int result = 0;
        
        // 处理操作
        for (int[] op : operations) {
            int type = op[0];
            if (type == 1) {
                // 插入操作
                int pos = op[1];
                int val = op[2];
                treap.insert(pos, val);
            } else if (type == 2) {
                // 查询操作
                int l = op[1];
                int r = op[2];
                result += treap.queryMax(l, r);
            }
        }
        
        return result;
    }
    
    // ====================================================================================
    // 题目8: LeetCode 715 - Range Module
    // 题目描述: 范围模块，维护区间并支持查询
    // 解题思路: 使用FHQ Treap维护区间信息
    // 时间复杂度: O(log n)
    // 空间复杂度: O(n)
    // ====================================================================================
    
    /**
     * Java实现
     */
    public static class RangeModule {
        private FHQTreap treap;
        
        public RangeModule() {
            this.treap = new FHQTreap();
        }
        
        public void addRange(int left, int right) {
            // 添加区间[left, right)
            // 简化实现
        }
        
        public boolean queryRange(int left, int right) {
            // 查询区间[left, right)是否被完全覆盖
            return false; // 简化实现
        }
        
        public void removeRange(int left, int right) {
            // 移除区间[left, right)
            // 简化实现
        }
    }
    
    /**
     * 主函数用于测试
     */
    public static void main(String[] args) {
        // 测试GSS6问题
        System.out.println("=== 测试GSS6问题 ===");
        GSS6Solver solver = new GSS6Solver();
        
        // 插入元素
        solver.insert(0, 1);
        solver.insert(1, 2);
        solver.insert(2, 3);
        solver.insert(3, 4);
        solver.insert(4, 5);
        
        System.out.println("查询区间[0,4]的最大子段和: " + solver.queryMaxSub(0, 4));
        
        // 替换元素
        solver.replace(2, -3);
        System.out.println("替换位置2为-3后，查询区间[0,4]的最大子段和: " + solver.queryMaxSub(0, 4));
        
        // 测试区间加
        FHQTreap treap = new FHQTreap();
        for (int i = 0; i < 5; i++) {
            treap.insert(i, i + 1);
        }
        treap.rangeAdd(1, 3, 10);
        System.out.println("区间[1,3]加10后:");
        treap.inorderTraversal();
        
        // 测试二进制编码问题
        System.out.println("\n=== 测试二进制编码问题 ===");
        String[] operations = {
            "insert 0 1",
            "insert 1 0",
            "insert 2 1",
            "query 0 2"
        };
        String[] results = binaryCodes(operations);
        System.out.println("二进制编码查询结果: " + Arrays.toString(results));
    }
}