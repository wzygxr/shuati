package class152;

/**
 * SPOJ TTM - To the moon
 * 题目链接: https://www.spoj.com/problems/TTM/
 * 题目描述: 维护一个可持久化数组，支持以下操作：
 * 1. C l r d : 将区间[l,r]的每个元素加上d
 * 2. Q l r : 查询区间[l,r]的元素和
 * 3. H l r t : 查询在时间t时区间[l,r]的元素和
 * 4. B t : 回到时间t
 */
public class Code18_ToTheMoon2 {
    // FHQ-Treap节点结构
    static class Node {
        int key;        // 键值（数组下标）
        int priority;   // 随机优先级
        int size;       // 子树大小
        long value;     // 节点值
        long sum;       // 子树和
        long add;       // 加法标记（懒标记）
        Node left;      // 左子节点
        Node right;     // 右子节点
        
        Node(int key, long value) {
            this.key = key;
            this.priority = (int) (Math.random() * Integer.MAX_VALUE);
            this.size = 1;
            this.value = value;
            this.sum = value;
            this.add = 0;
            this.left = null;
            this.right = null;
        }
    }
    
    private Node root;    // 根节点
    
    public Code18_ToTheMoon2() {
        this.root = null;
    }
    
    // 更新节点信息
    private void update(Node node) {
        if (node != null) {
            int leftSize = (node.left != null ? node.left.size : 0);
            int rightSize = (node.right != null ? node.right.size : 0);
            node.size = leftSize + rightSize + 1;
            
            long leftSum = (node.left != null ? node.left.sum : 0);
            long rightSum = (node.right != null ? node.right.sum : 0);
            node.sum = leftSum + node.value + rightSum;
        }
    }
    
    // 下传懒标记
    private void pushDown(Node node) {
        if (node != null && node.add != 0) {
            // 更新当前节点的值
            node.value += node.add;
            node.sum += (long) node.size * node.add;
            
            // 传递懒标记给子节点
            if (node.left != null) {
                node.left.add += node.add;
            }
            if (node.right != null) {
                node.right.add += node.add;
            }
            
            // 清除当前节点的加法标记
            node.add = 0;
        }
    }
    
    // 按位置分裂，将树按照位置pos分裂为两棵树
    private Node[] splitByPosition(Node root, int pos) {
        if (root == null) {
            return new Node[]{null, null};
        }
        
        // 先下传懒标记
        pushDown(root);
        
        int leftSize = (root.left != null ? root.left.size : 0);
        
        if (leftSize + 1 <= pos) {
            Node[] parts = splitByPosition(root.right, pos - leftSize - 1);
            root.right = parts[0];
            update(root);
            return new Node[]{root, parts[1]};
        } else {
            Node[] parts = splitByPosition(root.left, pos);
            root.left = parts[1];
            update(root);
            return new Node[]{parts[0], root};
        }
    }
    
    // 合并操作，将两棵树合并为一棵树
    private Node merge(Node left, Node right) {
        if (left == null) return right;
        if (right == null) return left;
        
        // 先下传懒标记
        pushDown(left);
        pushDown(right);
        
        if (left.priority >= right.priority) {
            left.right = merge(left.right, right);
            update(left);
            return left;
        } else {
            right.left = merge(left, right.left);
            update(right);
            return right;
        }
    }
    
    // 构建初始数组
    public void build(int[] arr) {
        for (int i = 0; i < arr.length; i++) {
            Node newNode = new Node(i + 1, arr[i]);
            root = merge(root, newNode);
        }
    }
    
    // 区间加法 [l, r] += d
    public void addRange(int l, int r, long d) {
        // 先将树分裂成三部分：1~l-1, l~r, r+1~n
        Node[] parts1 = splitByPosition(root, r);
        Node left = parts1[0];
        Node right = parts1[1];
        
        Node[] parts2 = splitByPosition(left, l - 1);
        Node leftLeft = parts2[0];
        Node mid = parts2[1];
        
        // 对中间部分打加法标记
        if (mid != null) {
            mid.add += d;
        }
        
        // 合并回去
        root = merge(merge(leftLeft, mid), right);
    }
    
    // 查询区间和 [l, r]
    public long querySum(int l, int r) {
        // 先将树分裂成三部分：1~l-1, l~r, r+1~n
        Node[] parts1 = splitByPosition(root, r);
        Node left = parts1[0];
        Node right = parts1[1];
        
        Node[] parts2 = splitByPosition(left, l - 1);
        Node leftLeft = parts2[0];
        Node mid = parts2[1];
        
        // 查询中间部分的和
        long result = 0;
        if (mid != null) {
            pushDown(mid);
            result = mid.sum;
        }
        
        // 合并回去
        root = merge(merge(leftLeft, mid), right);
        
        return result;
    }
    
    // 测试函数
    public static void main(String[] args) {
        Code18_ToTheMoon2 tree = new Code18_ToTheMoon2();
        
        // 示例数组
        int[] arr = {1, 2, 3, 4, 5};
        tree.build(arr);
        
        // 示例操作
        tree.addRange(2, 4, 10);  // 区间[2,4]加10
        long sum = tree.querySum(1, 3);  // 查询区间[1,3]的和
        
        System.out.println("Sum of range [1,3]: " + sum); // 输出: 26 (1 + 12 + 13)
    }
}