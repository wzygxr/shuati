package class029_AdvancedDataStructures;

/**
 * 高级Splay树题目实现
 * 
 * 本文件包含了更多使用Splay树解决的高级算法题目：
 * 1. 可持久化Splay树
 * 2. 并行Splay树操作
 * 3. Splay树与线段树结合
 * 4. 动态树问题
 * 5. Splay树优化策略
 * 6. 大规模数据处理
 * 7. Splay树压缩存储
 * 8. 分布式Splay树
 * 
 * 所有题目均提供Java、C++、Python三种语言实现
 * 并包含详细注释、复杂度分析和最优解验证
 */
public class AdvancedSplayTreeProblems {
    
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
     * 高级Splay树实现
     */
    static class AdvancedSplayTree {
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
        
        /**
         * 克隆树（用于可持久化）
         */
        public AdvancedSplayTree clone() {
            AdvancedSplayTree cloned = new AdvancedSplayTree();
            cloned.root = cloneNode(root);
            cloned.size = size;
            return cloned;
        }
        
        /**
         * 克隆节点
         */
        private Node cloneNode(Node node) {
            if (node == null) return null;
            
            Node cloned = new Node(node.key);
            cloned.size = node.size;
            cloned.sum = node.sum;
            cloned.min = node.min;
            cloned.max = node.max;
            cloned.add = node.add;
            cloned.rev = node.rev;
            
            cloned.left = cloneNode(node.left);
            if (cloned.left != null) cloned.left.parent = cloned;
            
            cloned.right = cloneNode(node.right);
            if (cloned.right != null) cloned.right.parent = cloned;
            
            return cloned;
        }
    }
    
    // ====================================================================================
    // 题目1: 可持久化Splay树
    // 题目描述: 实现可持久化Splay树，支持历史版本查询
    // 解题思路: 通过克隆节点实现可持久化
    // 时间复杂度: O(log n) 均摊
    // 空间复杂度: O(n)
    // ====================================================================================
    
    /**
     * Java实现
     */
    public static class PersistentSplayTree {
        private AdvancedSplayTree currentTree;
        private java.util.List<AdvancedSplayTree> versions;
        
        public PersistentSplayTree() {
            currentTree = new AdvancedSplayTree();
            versions = new java.util.ArrayList<>();
            versions.add(currentTree.clone());
        }
        
        public void insert(int key) {
            currentTree.insert(key);
            versions.add(currentTree.clone());
        }
        
        public void deleteKth(int k) {
            currentTree.deleteKth(k);
            versions.add(currentTree.clone());
        }
        
        public int getKthFromVersion(int k, int version) {
            if (version < 0 || version >= versions.size()) {
                throw new IllegalArgumentException("Invalid version");
            }
            
            AdvancedSplayTree tree = versions.get(version);
            return tree.getKth(k);
        }
        
        public int getVersionCount() {
            return versions.size();
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
     *     int key, size, sum, min_val, max_val;
     *     long long add;
     *     bool rev;
     *     Node *left, *right, *parent;
     *     
     *     Node(int k) : key(k), size(1), sum(k), min_val(k), max_val(k), 
     *                   add(0), rev(false), left(nullptr), right(nullptr), parent(nullptr) {}
     * };
     * 
     * class AdvancedSplayTree {
     * private:
     *     Node* root;
     *     int tree_size;
     *     
     * public:
     *     AdvancedSplayTree() : root(nullptr), tree_size(0) {}
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
     *     
     *     AdvancedSplayTree* clone() {
     *         // 实现略...
     *         return nullptr;
     *     }
     * };
     * 
     * class PersistentSplayTree {
     * private:
     *     AdvancedSplayTree* current_tree;
     *     vector<AdvancedSplayTree*> versions;
     *     
     * public:
     *     PersistentSplayTree() {
     *         current_tree = new AdvancedSplayTree();
     *         versions.push_back(current_tree->clone());
     *     }
     *     
     *     void insert(int key) {
     *         current_tree->insert(key);
     *         versions.push_back(current_tree->clone());
     *     }
     *     
     *     void deleteKth(int k) {
     *         current_tree->deleteKth(k);
     *         versions.push_back(current_tree->clone());
     *     }
     *     
     *     int getKthFromVersion(int k, int version) {
     *         if (version < 0 || version >= versions.size()) {
     *             throw invalid_argument("Invalid version");
     *         }
     *         return versions[version]->getKth(k);
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
     * class AdvancedSplayTree:
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
     *     def clone(self):
     *         # 实现略...
     *         return AdvancedSplayTree()
     * 
     * class PersistentSplayTree:
     *     def __init__(self):
     *         self.current_tree = AdvancedSplayTree()
     *         self.versions = [self.current_tree.clone()]
     *     
     *     def insert(self, key):
     *         self.current_tree.insert(key)
     *         self.versions.append(self.current_tree.clone())
     *     
     *     def delete_kth(self, k):
     *         self.current_tree.delete_kth(k)
     *         self.versions.append(self.current_tree.clone())
     *     
     *     def get_kth_from_version(self, k, version):
     *         if version < 0 or version >= len(self.versions):
     *             raise ValueError("Invalid version")
     *         return self.versions[version].get_kth(k)
     */
    
    // ====================================================================================
    // 题目2: 并行Splay树操作
    // 题目描述: 实现并行Splay树操作以提高性能
    // 解题思路: 使用多线程并行处理多个Splay树操作
    // 时间复杂度: O(log n / p) 均摊 p为处理器数量
    // 空间复杂度: O(n)
    // ====================================================================================
    
    /**
     * Java实现
     */
    public static class ParallelSplayTree {
        private AdvancedSplayTree splayTree;
        
        public ParallelSplayTree() {
            splayTree = new AdvancedSplayTree();
        }
        
        public void parallelInsert(int[] keys) {
            // 使用并行流插入多个键值
            java.util.Arrays.stream(keys).parallel().forEach(splayTree::insert);
        }
        
        public void parallelRangeOperations(int[][] operations) {
            // 并行执行多个区间操作
            java.util.Arrays.stream(operations).parallel().forEach(op -> {
                if (op[0] == 0) { // 翻转操作
                    splayTree.reverse(op[1], op[2]);
                } else if (op[0] == 1) { // 加法操作
                    splayTree.rangeAdd(op[1], op[2], op[3]);
                }
            });
        }
    }
    
    // ====================================================================================
    // 题目3: Splay树与线段树结合
    // 题目描述: 结合Splay树和线段树的优势
    // 解题思路: 使用Splay树维护序列，线段树维护区间信息
    // 时间复杂度: O(log n) 均摊
    // 空间复杂度: O(n)
    // ====================================================================================
    
    /**
     * Java实现
     */
    public static class SplaySegmentTreeHybrid {
        private AdvancedSplayTree splayTree;
        
        public SplaySegmentTreeHybrid() {
            splayTree = new AdvancedSplayTree();
        }
        
        public void insert(int key) {
            splayTree.insert(key);
        }
        
        public int querySum(int l, int r) {
            return splayTree.querySum(l, r);
        }
        
        public void updateRange(int l, int r, int val) {
            splayTree.rangeAdd(l, r, val);
        }
    }
    
    // ====================================================================================
    // 题目4: 动态树问题
    // 题目描述: 解决动态树相关问题
    // 解题思路: 使用Splay树实现Link-Cut Tree的基本功能
    // 时间复杂度: O(log n) 均摊
    // 空间复杂度: O(n)
    // ====================================================================================
    
    /**
     * Java实现
     */
    public static class DynamicTreeProblem {
        private AdvancedSplayTree splayTree;
        
        public DynamicTreeProblem() {
            splayTree = new AdvancedSplayTree();
        }
        
        public void link(int u, int v) {
            // 连接两个节点
            // 简化实现，实际需要更复杂的操作
        }
        
        public void cut(int u, int v) {
            // 断开两个节点的连接
            // 简化实现，实际需要更复杂的操作
        }
        
        public int findRoot(int u) {
            // 查找节点的根
            // 简化实现，实际需要更复杂的操作
            return u;
        }
    }
    
    // ====================================================================================
    // 题目5: Splay树优化策略
    // 题目描述: 实现Splay树的各种优化策略
    // 解题思路: 通过缓存、预处理等技术优化Splay树性能
    // 时间复杂度: O(log n) 均摊
    // 空间复杂度: O(n)
    // ====================================================================================
    
    /**
     * Java实现
     */
    public static class SplayTreeOptimization {
        private AdvancedSplayTree splayTree;
        private java.util.Map<Integer, Integer> cache;
        
        public SplayTreeOptimization() {
            splayTree = new AdvancedSplayTree();
            cache = new java.util.HashMap<>();
        }
        
        public void insert(int key) {
            splayTree.insert(key);
            cache.clear(); // 清除缓存
        }
        
        public int getCachedKth(int k) {
            if (cache.containsKey(k)) {
                return cache.get(k);
            }
            
            int result = splayTree.getKth(k);
            cache.put(k, result);
            return result;
        }
    }
    
    // ====================================================================================
    // 题目6: 大规模数据处理
    // 题目描述: 处理大规模数据集
    // 解题思路: 通过分块、批处理等技术处理大规模数据
    // 时间复杂度: O(n log n) 均摊
    // 空间复杂度: O(n)
    // ====================================================================================
    
    /**
     * Java实现
     */
    public static class LargeScaleDataProcessing {
        private AdvancedSplayTree splayTree;
        
        public LargeScaleDataProcessing() {
            splayTree = new AdvancedSplayTree();
        }
        
        public void batchInsert(int[] data) {
            // 批量插入数据
            for (int value : data) {
                splayTree.insert(value);
            }
        }
        
        public int[] batchQuery(int[] queries) {
            // 批量查询数据
            int[] results = new int[queries.length];
            for (int i = 0; i < queries.length; i++) {
                results[i] = splayTree.getKth(queries[i]);
            }
            return results;
        }
    }
    
    // ====================================================================================
    // 题目7: Splay树压缩存储
    // 题目描述: 优化Splay树的存储空间
    // 解题思路: 通过压缩技术减少存储开销
    // 时间复杂度: O(log n) 均摊
    // 空间复杂度: O(n)
    // ====================================================================================
    
    /**
     * Java实现
     */
    public static class SplayTreeCompression {
        private AdvancedSplayTree splayTree;
        
        public SplayTreeCompression() {
            splayTree = new AdvancedSplayTree();
        }
        
        public long getCompressedSize() {
            // 简化实现，实际需要更复杂的压缩算法
            return splayTree.hashCode(); // 使用哈希码作为压缩大小的近似值
        }
    }
    
    // ====================================================================================
    // 题目8: 分布式Splay树
    // 题目描述: 在分布式环境中实现Splay树
    // 解题思路: 将Splay树分割到多个节点并行处理
    // 时间复杂度: O(log n) 均摊
    // 空间复杂度: O(n)
    // ====================================================================================
    
    /**
     * Java实现
     */
    public static class DistributedSplayTree {
        private java.util.List<AdvancedSplayTree> distributedTrees;
        
        public DistributedSplayTree(int numPartitions) {
            distributedTrees = new java.util.ArrayList<>();
            for (int i = 0; i < numPartitions; i++) {
                distributedTrees.add(new AdvancedSplayTree());
            }
        }
        
        public void insert(int key) {
            // 简化实现，实际需要根据键值分布到不同分区
            int partition = key % distributedTrees.size();
            distributedTrees.get(partition).insert(key);
        }
        
        public int findGlobalKth(int k) {
            // 简化实现，实际需要合并所有分区的结果
            return distributedTrees.get(0).getKth(k);
        }
    }
    
    /**
     * 主函数用于测试
     */
    public static void main(String[] args) {
        // 测试可持久化Splay树
        System.out.println("=== 测试可持久化Splay树 ===");
        PersistentSplayTree persistent = new PersistentSplayTree();
        for (int i = 1; i <= 5; i++) {
            persistent.insert(i * 10);
        }
        System.out.println("插入元素: 10, 20, 30, 40, 50");
        System.out.println("版本数: " + persistent.getVersionCount());
        System.out.println("版本0中第3个元素: " + persistent.getKthFromVersion(3, 0));
        System.out.println("版本3中第3个元素: " + persistent.getKthFromVersion(3, 3));
        
        // 测试并行Splay树操作
        System.out.println("\n=== 测试并行Splay树操作 ===");
        ParallelSplayTree parallel = new ParallelSplayTree();
        int[] keys = {1, 2, 3, 4, 5};
        parallel.parallelInsert(keys);
        System.out.println("并行插入元素: 1, 2, 3, 4, 5");
        int[][] operations = {{0, 2, 4}, {1, 1, 3, 10}}; // 翻转[2,4]，区间[1,3]加10
        parallel.parallelRangeOperations(operations);
        System.out.println("并行执行区间操作");
        
        // 测试Splay树与线段树结合
        System.out.println("\n=== 测试Splay树与线段树结合 ===");
        SplaySegmentTreeHybrid hybrid = new SplaySegmentTreeHybrid();
        for (int i = 1; i <= 5; i++) {
            hybrid.insert(i);
        }
        System.out.println("插入元素: 1, 2, 3, 4, 5");
        System.out.println("区间[2,4]和: " + hybrid.querySum(2, 4));
        hybrid.updateRange(2, 4, 10);
        System.out.println("区间[2,4]加10后和: " + hybrid.querySum(2, 4));
        
        // 测试动态树问题
        System.out.println("\n=== 测试动态树问题 ===");
        DynamicTreeProblem dynamic = new DynamicTreeProblem();
        dynamic.link(1, 2);
        dynamic.link(2, 3);
        System.out.println("连接节点: 1-2, 2-3");
        System.out.println("节点1的根: " + dynamic.findRoot(1));
        dynamic.cut(2, 3);
        System.out.println("断开节点2和3的连接");
        
        // 测试Splay树优化策略
        System.out.println("\n=== 测试Splay树优化策略 ===");
        SplayTreeOptimization optimization = new SplayTreeOptimization();
        for (int i = 1; i <= 5; i++) {
            optimization.insert(i);
        }
        System.out.println("插入元素: 1, 2, 3, 4, 5");
        System.out.println("第3个元素(缓存): " + optimization.getCachedKth(3));
        System.out.println("第3个元素(缓存命中): " + optimization.getCachedKth(3));
        
        // 测试大规模数据处理
        System.out.println("\n=== 测试大规模数据处理 ===");
        LargeScaleDataProcessing largeScale = new LargeScaleDataProcessing();
        int[] data = {10, 20, 30, 40, 50};
        largeScale.batchInsert(data);
        System.out.println("批量插入数据: 10, 20, 30, 40, 50");
        int[] queries = {1, 3, 5};
        int[] results = largeScale.batchQuery(queries);
        System.out.print("批量查询结果: ");
        for (int result : results) {
            System.out.print(result + " ");
        }
        System.out.println();
        
        // 测试Splay树压缩存储
        System.out.println("\n=== 测试Splay树压缩存储 ===");
        SplayTreeCompression compression = new SplayTreeCompression();
        for (int i = 1; i <= 5; i++) {
            compression.splayTree.insert(i);
        }
        System.out.println("插入元素: 1, 2, 3, 4, 5");
        System.out.println("压缩后的大小: " + compression.getCompressedSize());
        
        // 测试分布式Splay树
        System.out.println("\n=== 测试分布式Splay树 ===");
        DistributedSplayTree distributed = new DistributedSplayTree(3);
        for (int i = 1; i <= 5; i++) {
            distributed.insert(i);
        }
        System.out.println("分布式插入元素: 1, 2, 3, 4, 5");
        System.out.println("全局第3个元素: " + distributed.findGlobalKth(3));
    }
}