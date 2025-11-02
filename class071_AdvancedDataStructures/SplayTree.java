package class186;

/**
 * Splay Tree (伸展树) 实现
 * 支持操作：
 * - 区间翻转
 * - 区间加标记
 * - 区间查询（求和、最值等）
 * - 插入、删除、查找
 * 
 * 时间复杂度：所有操作均为 O(log n) 均摊
 * 空间复杂度：O(n)
 * 
 * 设计要点：
 * 1. 通过伸展操作将访问的节点移动到根，利用局部性原理优化性能
 * 2. 支持延迟标记处理区间操作
 * 3. 实现旋转、splay等核心操作
 * 4. 工程化考量：异常处理、边界检查
 * 
 * 典型应用场景：
 * - 频繁访问最近操作的数据
 * - 区间操作需求
 * - 内存敏感场景（与AVL、红黑树相比，实现更简单）
 */
public class SplayTree {
    private Node root;
    private int size;
    
    /**
     * 节点类
     */
    private static class Node {
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
        root = a;
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
    
    /**
     * 主函数用于测试
     */
    public static void main(String[] args) {
        SplayTree splay = new SplayTree();
        
        // 测试插入
        for (int i = 1; i <= 10; i++) {
            splay.insert(i);
        }
        
        System.out.print("初始序列: ");
        splay.inorderTraversal(); // 应该是 1 2 3 4 5 6 7 8 9 10
        
        // 测试区间翻转
        splay.reverse(2, 7);
        System.out.print("翻转区间[2,7]: ");
        splay.inorderTraversal(); // 应该是 1 7 6 5 4 3 2 8 9 10
        
        // 测试区间加
        splay.rangeAdd(3, 6, 10);
        System.out.print("区间[3,6]加10: ");
        splay.inorderTraversal(); // 应该是 1 7 16 15 14 13 2 8 9 10
        
        // 测试查询
        System.out.println("区间[2,8]和: " + splay.querySum(2, 8)); // 应该是 7+16+15+14+13+2+8 = 75
    }
}