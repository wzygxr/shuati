package class186;

/**
 * FHQ-Treap (无旋Treap) 实现
 * 支持操作：
 * - 序列维护
 * - 区间翻转
 * - 区间加标记
 * - 区间查询（求和、最值等）
 * 
 * 时间复杂度：所有操作均为 O(log n) 均摊
 * 空间复杂度：O(n)
 * 
 * 设计要点：
 * 1. 利用随机优先级维护树的平衡
 * 2. 支持split和merge两个核心操作
 * 3. 延迟标记处理区间操作
 * 4. 工程化考量：异常处理、边界检查
 * 
 * 典型应用场景：
 * - 序列区间操作
 * - 动态维护有序序列
 * - 需要支持分裂和合并的场景
 */
import java.util.Random;

public class FHQTreap {
    private Node root;
    private static Random rand = new Random();
    private int size;
    
    /**
     * 节点类
     */
    private static class Node {
        int key;          // 节点值
        int priority;     // 随机优先级
        int size;         // 子树大小
        int sum;          // 子树和
        int min;          // 子树最小值
        int max;          // 子树最大值
        long add;         // 加法标记
        boolean rev;      // 翻转标记
        Node left, right; // 左右子树
        
        public Node(int key) {
            this.key = key;
            this.priority = rand.nextInt();
            this.size = 1;
            this.sum = key;
            this.min = key;
            this.max = key;
            this.add = 0;
            this.rev = false;
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
            res2[1].sum += val * res2[1].size;
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
    public int querySum(int l, int r) {
        if (l < 0 || r >= size || l > r) {
            throw new IllegalArgumentException("Invalid range");
        }
        
        Node[] res1 = new Node[2];
        Node[] res2 = new Node[2];
        
        split(root, r + 1, res1);
        split(res1[0], l, res2);
        
        int sum = res2[1] == null ? 0 : res2[1].sum;
        
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
    
    /**
     * 主函数用于测试
     */
    public static void main(String[] args) {
        FHQTreap treap = new FHQTreap();
        
        // 测试插入
        for (int i = 0; i < 10; i++) {
            treap.insert(i, i + 1);
        }
        
        System.out.print("初始序列: ");
        treap.inorderTraversal(); // 应该是 1 2 3 4 5 6 7 8 9 10
        
        // 测试区间翻转
        treap.reverse(2, 7);
        System.out.print("翻转区间[2,7]: ");
        treap.inorderTraversal(); // 应该是 1 2 8 7 6 5 4 3 9 10
        
        // 测试区间加
        treap.rangeAdd(3, 6, 10);
        System.out.print("区间[3,6]加10: ");
        treap.inorderTraversal(); // 应该是 1 2 8 17 16 15 14 3 9 10
        
        // 测试查询
        System.out.println("区间[2,8]和: " + treap.querySum(2, 8)); // 应该是 8+17+16+15+14+3+9 = 82
        System.out.println("区间[2,8]最小值: " + treap.queryMin(2, 8)); // 应该是 3
        System.out.println("区间[2,8]最大值: " + treap.queryMax(2, 8)); // 应该是 17
        
        // 测试删除
        treap.delete(4);
        System.out.print("删除位置4后: ");
        treap.inorderTraversal(); // 应该是 1 2 8 17 15 14 3 9 10
    }
}