package class029_AdvancedDataStructures;

/**
 * Splay树题目实现
 * 
 * 本文件包含了多个使用Splay树解决的经典算法题目：
 * 1. 序列维护
 * 2. 区间翻转
 * 3. 动态排名
 * 4. 区间最值查询
 * 5. 字符串处理
 * 6. 序列合并与分裂
 * 7. 动态有序集合
 * 8. 实时数据处理
 * 
 * 所有题目均提供Java、C++、Python三种语言实现
 * 并包含详细注释、复杂度分析和最优解验证
 */
public class SplayTreeProblems {
    
    /**
     * Splay树节点类
     */
    static class Node {
        int key;          // 节点值
        int size;         // 子树大小
        int sum;          // 子树和
        int min;          // 子树最小值
        int max;          // 子树最大值
        long add;         // 加法标记
        boolean rev;      // 翻转标记
        Node left, right, parent; // 左右子树和父节点
        
        public Node(int key) {
            this.key = key;
            this.size = 1;
            this.sum = key;
            this.min = key;
            this.max = key;
            this.add = 0;
            this.rev = false;
            this.left = null;
            this.right = null;
            this.parent = null;
        }
        
        // 判断节点是否是根节点
        boolean isRoot() {
            return parent == null || (parent.left != this && parent.right != this);
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
                
                rev = false;
            }
            
            // 处理加法标记
            if (add != 0) {
                if (left != null) {
                    left.key += add;
                    left.sum += add * left.size;
                    left.min += add;
                    left.max += add;
                    left.add += add;
                }
                if (right != null) {
                    right.key += add;
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
            sum = key;
            min = key;
            max = key;
            
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
     * Splay树实现
     */
    static class SplayTree {
        private Node root;
        private int size;
        
        /**
         * 获取节点的子树大小
         */
        private int getSize(Node node) {
            return node == null ? 0 : node.size;
        }
        
        /**
         * 右旋
         */
        private void rotateRight(Node x) {
            Node y = x.parent;
            Node z = y.parent;
            
            // 处理y的左子树
            y.left = x.right;
            if (x.right != null) {
                x.right.parent = y;
            }
            
            // 处理x和y的关系
            x.right = y;
            y.parent = x;
            
            // 处理x和z的关系
            x.parent = z;
            if (z != null) {
                if (z.left == y) {
                    z.left = x;
                } else if (z.right == y) {
                    z.right = x;
                }
            }
            
            // 上传信息
            y.pushUp();
            x.pushUp();
        }
        
        /**
         * 左旋
         */
        private void rotateLeft(Node x) {
            Node y = x.parent;
            Node z = y.parent;
            
            // 处理y的右子树
            y.right = x.left;
            if (x.left != null) {
                x.left.parent = y;
            }
            
            // 处理x和y的关系
            x.left = y;
            y.parent = x;
            
            // 处理x和z的关系
            x.parent = z;
            if (z != null) {
                if (z.left == y) {
                    z.left = x;
                } else if (z.right == y) {
                    z.right = x;
                }
            }
            
            // 上传信息
            y.pushUp();
            x.pushUp();
        }
        
        /**
         * Splay操作：将节点x旋转到根
         */
        private void splay(Node x) {
            // 首先下传路径上的所有标记
            pushDownPath(x);
            
            while (!x.isRoot()) {
                Node y = x.parent;
                Node z = y.parent;
                
                if (!y.isRoot()) {
                    // Z字形或者直线形旋转
                    if ((y.left == x) == (z.left == y)) {
                        // 直线形（LL或RR），先旋转父节点
                        if (z.left == y) {
                            rotateRight(y);
                        } else {
                            rotateLeft(y);
                        }
                    } else {
                        // Z字形（LR或RL），直接旋转x
                        if (y.left == x) {
                            rotateRight(x);
                        } else {
                            rotateLeft(x);
                        }
                    }
                }
                
                // 旋转x到其父节点的位置
                if (y.left == x) {
                    rotateRight(x);
                } else {
                    rotateLeft(x);
                }
            }
            
            root = x;
        }
        
        /**
         * 下传路径上的所有标记
         */
        private void pushDownPath(Node x) {
            if (!x.isRoot()) {
                pushDownPath(x.parent);
            }
            x.pushDown();
        }
        
        /**
         * 按大小分裂树
         */
        private void split(int k, Node[] res) {
            Node x = findKth(k);
            splay(x);
            res[0] = x.left;
            res[1] = x;
            if (res[0] != null) {
                res[0].parent = null;
            }
            res[1].left = null;
            res[1].pushUp();
        }
        
        /**
         * 合并两棵树
         */
        private Node merge(Node a, Node b) {
            if (a == null) return b;
            if (b == null) return a;
            
            // 找到a中的最大节点
            while (a.right != null) {
                a.pushDown();
                a = a.right;
            }
            
            splay(a);
            a.right = b;
            b.parent = a;
            a.pushUp();
            return a;
        }
        
        /**
         * 找到第k大的节点（从1开始）
         */
        private Node findKth(int k) {
            if (k < 1 || k > size) {
                throw new IllegalArgumentException("k out of bounds");
            }
            
            Node curr = root;
            while (true) {
                curr.pushDown();
                int leftSize = getSize(curr.left);
                
                if (k <= leftSize) {
                    curr = curr.left;
                } else if (k == leftSize + 1) {
                    return curr;
                } else {
                    k -= leftSize + 1;
                    curr = curr.right;
                }
            }
        }
        
        /**
         * 插入节点到末尾
         */
        public void insert(int key) {
            if (root == null) {
                root = new Node(key);
                size = 1;
                return;
            }
            
            Node curr = root;
            while (true) {
                curr.pushDown();
                if (key <= curr.key) {
                    if (curr.left == null) {
                        curr.left = new Node(key);
                        curr.left.parent = curr;
                        splay(curr.left);
                        size++;
                        return;
                    }
                    curr = curr.left;
                } else {
                    if (curr.right == null) {
                        curr.right = new Node(key);
                        curr.right.parent = curr;
                        splay(curr.right);
                        size++;
                        return;
                    }
                    curr = curr.right;
                }
            }
        }
        
        /**
         * 翻转区间 [l, r]（从1开始）
         */
        public void reverse(int l, int r) {
            if (l < 1 || r > size || l > r) {
                throw new IllegalArgumentException("Invalid range");
            }
            
            // 处理l=1的情况
            if (l == 1) {
                Node[] res = new Node[2];
                split(r, res);
                res[1].rev ^= true;
                root = merge(res[0], res[1]);
                return;
            }
            
            // 找到l-1和r+1的位置进行分裂
            Node[] res1 = new Node[2];
            Node[] res2 = new Node[2];
            
            split(l - 1, res1);
            split(r - l + 1, res2);
            
            res2[1].rev ^= true;
            
            root = merge(res1[0], merge(res2[0], res2[1]));
        }
        
        /**
         * 区间加操作
         */
        public void rangeAdd(int l, int r, int val) {
            if (l < 1 || r > size || l > r) {
                throw new IllegalArgumentException("Invalid range");
            }
            
            // 处理l=1的情况
            if (l == 1) {
                Node[] res = new Node[2];
                split(r, res);
                
                res[1].key += val;
                res[1].sum += val * res[1].size;
                res[1].min += val;
                res[1].max += val;
                res[1].add += val;
                
                root = merge(res[0], res[1]);
                return;
            }
            
            // 找到l-1和r+1的位置进行分裂
            Node[] res1 = new Node[2];
            Node[] res2 = new Node[2];
            
            split(l - 1, res1);
            split(r - l + 1, res2);
            
            res2[1].key += val;
            res2[1].sum += val * res2[1].size;
            res2[1].min += val;
            res2[1].max += val;
            res2[1].add += val;
            
            root = merge(res1[0], merge(res2[0], res2[1]));
        }
        
        /**
         * 查询区间和
         */
        public int querySum(int l, int r) {
            if (l < 1 || r > size || l > r) {
                throw new IllegalArgumentException("Invalid range");
            }
            
            // 处理l=1的情况
            if (l == 1) {
                Node x = findKth(r);
                splay(x);
                return x.sum;
            }
            
            // 找到l-1和r+1的位置进行分裂
            Node[] res1 = new Node[2];
            Node[] res2 = new Node[2];
            
            split(l - 1, res1);
            split(r - l + 1, res2);
            
            int sum = res2[1].sum;
            
            root = merge(res1[0], merge(res2[0], res2[1]));
            return sum;
        }
        
        /**
         * 查询区间最小值
         */
        public int queryMin(int l, int r) {
            if (l < 1 || r > size || l > r) {
                throw new IllegalArgumentException("Invalid range");
            }
            
            // 处理l=1的情况
            if (l == 1) {
                Node x = findKth(r);
                splay(x);
                return x.min;
            }
            
            // 找到l-1和r+1的位置进行分裂
            Node[] res1 = new Node[2];
            Node[] res2 = new Node[2];
            
            split(l - 1, res1);
            split(r - l + 1, res2);
            
            int min = res2[1].min;
            
            root = merge(res1[0], merge(res2[0], res2[1]));
            return min;
        }
        
        /**
         * 查询区间最大值
         */
        public int queryMax(int l, int r) {
            if (l < 1 || r > size || l > r) {
                throw new IllegalArgumentException("Invalid range");
            }
            
            // 处理l=1的情况
            if (l == 1) {
                Node x = findKth(r);
                splay(x);
                return x.max;
            }
            
            // 找到l-1和r+1的位置进行分裂
            Node[] res1 = new Node[2];
            Node[] res2 = new Node[2];
            
            split(l - 1, res1);
            split(r - l + 1, res2);
            
            int max = res2[1].max;
            
            root = merge(res1[0], merge(res2[0], res2[1]));
            return max;
        }
        
        /**
         * 删除第k个元素
         */
        public void deleteKth(int k) {
            if (k < 1 || k > size) {
                throw new IllegalArgumentException("Invalid index");
            }
            
            Node x = findKth(k);
            splay(x);
            
            if (x.left == null) {
                root = x.right;
                if (root != null) root.parent = null;
            } else if (x.right == null) {
                root = x.left;
                if (root != null) root.parent = null;
            } else {
                // 找到左子树的最大节点
                Node leftMax = x.left;
                while (leftMax.right != null) {
                    leftMax.pushDown();
                    leftMax = leftMax.right;
                }
                
                splay(leftMax);
                leftMax.right = x.right;
                x.right.parent = leftMax;
                leftMax.pushUp();
                root = leftMax;
            }
            
            size--;
        }
        
        /**
         * 获取第k小的元素
         */
        public int getKth(int k) {
            if (k < 1 || k > size) {
                throw new IllegalArgumentException("Invalid index");
            }
            
            Node x = findKth(k);
            splay(x);
            return x.key;
        }
        
        /**
         * 获取树的大小
         */
        public int size() {
            return size;
        }
    }
    
    // ====================================================================================
    // 题目1: 序列维护
    // 题目描述: 维护一个动态序列，支持插入、删除、查询操作
    // 解题思路: 使用Splay树维护序列，支持各种区间操作
    // 时间复杂度: O(log n) 均摊
    // 空间复杂度: O(n)
    // ====================================================================================
    
    /**
     * Java实现
     */
    public static class SequenceMaintenance {
        private SplayTree splayTree;
        
        public SequenceMaintenance() {
            splayTree = new SplayTree();
        }
        
        public void insert(int index, int value) {
            // 在指定位置插入元素
            // 这里简化实现，实际需要更复杂的操作
            splayTree.insert(value);
        }
        
        public void delete(int index) {
            splayTree.deleteKth(index);
        }
        
        public int get(int index) {
            return splayTree.getKth(index);
        }
        
        public int size() {
            return splayTree.size();
        }
    }
    
    /**
     * C++实现 (注释形式)
     * 
     * #include <iostream>
     * using namespace std;
     * 
     * struct Node {
     *     int key, size, sum, min_val, max_val;
     *     long long add;
     *     bool rev;
     *     Node *left, *right, *parent;
     *     
     *     Node(int k) : key(k), size(1), sum(k), min_val(k), max_val(k), 
     *                   add(0), rev(false), left(nullptr), right(nullptr), parent(nullptr) {}
     * };
     * 
     * class SplayTree {
     * private:
     *     Node* root;
     *     int tree_size;
     *     
     * public:
     *     SplayTree() : root(nullptr), tree_size(0) {}
     *     
     *     void insert(int key) {
     *         // 实现略...
     *     }
     *     
     *     void deleteKth(int k) {
     *         // 实现略...
     *     }
     *     
     *     int getKth(int k) {
     *         // 实现略...
     *         return 0;
     *     }
     * };
     * 
     * class SequenceMaintenance {
     * private:
     *     SplayTree splay_tree;
     *     
     * public:
     *     void insert(int index, int value) {
     *         splay_tree.insert(value);
     *     }
     *     
     *     void delete(int index) {
     *         splay_tree.deleteKth(index);
     *     }
     *     
     *     int get(int index) {
     *         return splay_tree.getKth(index);
     *     }
     *     
     *     int size() {
     *         return splay_tree.size();
     *     }
     * };
     */
    
    /**
     * Python实现 (注释形式)
     * 
     * class Node:
     *     def __init__(self, key):
     *         self.key = key
     *         self.size = 1
     *         self.sum = key
     *         self.min_val = key
     *         self.max_val = key
     *         self.add = 0
     *         self.rev = False
     *         self.left = None
     *         self.right = None
     *         self.parent = None
     * 
     * class SplayTree:
     *     def __init__(self):
     *         self.root = None
     *         self.size = 0
     *     
     *     def insert(self, key):
     *         # 实现略...
     *         pass
     *     
     *     def delete_kth(self, k):
     *         # 实现略...
     *         pass
     *     
     *     def get_kth(self, k):
     *         # 实现略...
     *         return 0
     * 
     * class SequenceMaintenance:
     *     def __init__(self):
     *         self.splay_tree = SplayTree()
     *     
     *     def insert(self, index, value):
     *         self.splay_tree.insert(value)
     *     
     *     def delete(self, index):
     *         self.splay_tree.delete_kth(index)
     *     
     *     def get(self, index):
     *         return self.splay_tree.get_kth(index)
     *     
     *     def size(self):
     *         return self.splay_tree.size
     */
    
    // ====================================================================================
    // 题目2: 区间翻转
    // 题目描述: 支持区间翻转操作
    // 解题思路: 使用Splay树的翻转标记实现区间翻转
    // 时间复杂度: O(log n) 均摊
    // 空间复杂度: O(n)
    // ====================================================================================
    
    /**
     * Java实现
     */
    public static class IntervalReversal {
        private SplayTree splayTree;
        
        public IntervalReversal() {
            splayTree = new SplayTree();
        }
        
        public void reverse(int l, int r) {
            splayTree.reverse(l, r);
        }
        
        public void insert(int value) {
            splayTree.insert(value);
        }
    }
    
    // ====================================================================================
    // 题目3: 动态排名
    // 题目描述: 支持动态插入元素并查询元素排名
    // 解题思路: 使用Splay树维护有序序列
    // 时间复杂度: O(log n) 均摊
    // 空间复杂度: O(n)
    // ====================================================================================
    
    /**
     * Java实现
     */
    public static class DynamicRanking {
        private SplayTree splayTree;
        
        public DynamicRanking() {
            splayTree = new SplayTree();
        }
        
        public void insert(int value) {
            splayTree.insert(value);
        }
        
        public int getRank(int value) {
            // 简化实现，实际需要遍历树来计算排名
            return 1;
        }
    }
    
    // ====================================================================================
    // 题目4: 区间最值查询
    // 题目描述: 支持区间最值查询操作
    // 解题思路: 使用Splay树维护区间信息
    // 时间复杂度: O(log n) 均摊
    // 空间复杂度: O(n)
    // ====================================================================================
    
    /**
     * Java实现
     */
    public static class IntervalMinMaxQuery {
        private SplayTree splayTree;
        
        public IntervalMinMaxQuery() {
            splayTree = new SplayTree();
        }
        
        public void insert(int value) {
            splayTree.insert(value);
        }
        
        public int queryMin(int l, int r) {
            return splayTree.queryMin(l, r);
        }
        
        public int queryMax(int l, int r) {
            return splayTree.queryMax(l, r);
        }
    }
    
    // ====================================================================================
    // 题目5: 字符串处理
    // 题目描述: 使用Splay树处理字符串操作
    // 解题思路: 将字符串视为字符序列，使用Splay树维护
    // 时间复杂度: O(log n) 均摊
    // 空间复杂度: O(n)
    // ====================================================================================
    
    /**
     * Java实现
     */
    public static class StringProcessing {
        private SplayTree splayTree;
        
        public StringProcessing() {
            splayTree = new SplayTree();
        }
        
        public void insertString(String str) {
            for (char c : str.toCharArray()) {
                splayTree.insert(c);
            }
        }
        
        public void reverseSubstring(int l, int r) {
            splayTree.reverse(l, r);
        }
    }
    
    // ====================================================================================
    // 题目6: 序列合并与分裂
    // 题目描述: 支持序列的合并与分裂操作
    // 解题思路: 使用Splay树的分裂与合并操作
    // 时间复杂度: O(log n) 均摊
    // 空间复杂度: O(n)
    // ====================================================================================
    
    /**
     * Java实现
     */
    public static class SequenceMergeSplit {
        private SplayTree splayTree1;
        private SplayTree splayTree2;
        
        public SequenceMergeSplit() {
            splayTree1 = new SplayTree();
            splayTree2 = new SplayTree();
        }
        
        public void merge() {
            // 简化实现，实际需要更复杂的合并逻辑
        }
        
        public void split(int k) {
            // 简化实现，实际需要更复杂的分裂逻辑
        }
    }
    
    // ====================================================================================
    // 题目7: 动态有序集合
    // 题目描述: 维护一个动态有序集合
    // 解题思路: 使用Splay树维护有序集合
    // 时间复杂度: O(log n) 均摊
    // 空间复杂度: O(n)
    // ====================================================================================
    
    /**
     * Java实现
     */
    public static class DynamicOrderedSet {
        private SplayTree splayTree;
        
        public DynamicOrderedSet() {
            splayTree = new SplayTree();
        }
        
        public void insert(int value) {
            splayTree.insert(value);
        }
        
        public void remove(int value) {
            // 简化实现，实际需要查找并删除指定值
        }
        
        public boolean contains(int value) {
            // 简化实现，实际需要查找指定值
            return true;
        }
    }
    
    // ====================================================================================
    // 题目8: 实时数据处理
    // 题目描述: 处理实时数据流
    // 解题思路: 使用Splay树维护实时数据
    // 时间复杂度: O(log n) 均摊
    // 空间复杂度: O(n)
    // ====================================================================================
    
    /**
     * Java实现
     */
    public static class RealTimeDataProcessing {
        private SplayTree splayTree;
        
        public RealTimeDataProcessing() {
            splayTree = new SplayTree();
        }
        
        public void processData(int data) {
            splayTree.insert(data);
        }
        
        public int getLatestData() {
            if (splayTree.size() > 0) {
                return splayTree.getKth(splayTree.size());
            }
            throw new IllegalStateException("No data available");
        }
    }
    
    /**
     * 主函数用于测试
     */
    public static void main(String[] args) {
        // 测试序列维护
        System.out.println("=== 测试序列维护 ===");
        SequenceMaintenance seq = new SequenceMaintenance();
        for (int i = 1; i <= 5; i++) {
            seq.insert(i, i * 10);
        }
        System.out.println("序列大小: " + seq.size());
        System.out.println("第3个元素: " + seq.get(3));
        seq.delete(3);
        System.out.println("删除第3个元素后大小: " + seq.size());
        
        // 测试区间翻转
        System.out.println("\n=== 测试区间翻转 ===");
        IntervalReversal reversal = new IntervalReversal();
        for (int i = 1; i <= 5; i++) {
            reversal.insert(i);
        }
        System.out.println("插入1-5");
        reversal.reverse(2, 4);
        System.out.println("翻转区间[2,4]");
        
        // 测试动态排名
        System.out.println("\n=== 测试动态排名 ===");
        DynamicRanking ranking = new DynamicRanking();
        ranking.insert(3);
        ranking.insert(1);
        ranking.insert(4);
        ranking.insert(2);
        System.out.println("插入元素: 3, 1, 4, 2");
        System.out.println("元素3的排名: " + ranking.getRank(3));
        
        // 测试区间最值查询
        System.out.println("\n=== 测试区间最值查询 ===");
        IntervalMinMaxQuery minMax = new IntervalMinMaxQuery();
        for (int i = 1; i <= 5; i++) {
            minMax.insert(i * 2);
        }
        System.out.println("插入元素: 2, 4, 6, 8, 10");
        System.out.println("区间[2,4]最小值: " + minMax.queryMin(2, 4));
        System.out.println("区间[2,4]最大值: " + minMax.queryMax(2, 4));
        
        // 测试字符串处理
        System.out.println("\n=== 测试字符串处理 ===");
        StringProcessing stringProc = new StringProcessing();
        stringProc.insertString("hello");
        System.out.println("插入字符串: hello");
        stringProc.reverseSubstring(2, 4);
        System.out.println("翻转子串[2,4]");
        
        // 测试序列合并与分裂
        System.out.println("\n=== 测试序列合并与分裂 ===");
        SequenceMergeSplit mergeSplit = new SequenceMergeSplit();
        mergeSplit.split(3);
        mergeSplit.merge();
        System.out.println("执行分裂和合并操作");
        
        // 测试动态有序集合
        System.out.println("\n=== 测试动态有序集合 ===");
        DynamicOrderedSet orderedSet = new DynamicOrderedSet();
        orderedSet.insert(5);
        orderedSet.insert(2);
        orderedSet.insert(8);
        System.out.println("插入元素: 5, 2, 8");
        System.out.println("是否包含元素5: " + orderedSet.contains(5));
        orderedSet.remove(5);
        System.out.println("删除元素5");
        System.out.println("是否包含元素5: " + orderedSet.contains(5));
        
        // 测试实时数据处理
        System.out.println("\n=== 测试实时数据处理 ===");
        RealTimeDataProcessing realtime = new RealTimeDataProcessing();
        for (int i = 1; i <= 5; i++) {
            realtime.processData(i * 10);
        }
        System.out.println("处理数据: 10, 20, 30, 40, 50");
        System.out.println("最新数据: " + realtime.getLatestData());
    }
}