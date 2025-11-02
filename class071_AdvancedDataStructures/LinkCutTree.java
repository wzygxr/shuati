package class186;

/**
 * Link-Cut Tree (LCT) 实现
 * 支持操作：
 * - 连边/断边
 * - 路径求和/最值/异或
 * - 子树查询
 * 时间复杂度：所有操作均为 O(log n) 均摊
 * 空间复杂度：O(n)
 */
public class LinkCutTree {
    private Node[] nodes;
    private int n;

    /**
     * 节点类
     */
    private static class Node {
        int val;         // 节点值
        int sum;         // 子树和
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
    public int querySum(int u, int v) {
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
     * 主函数用于测试
     */
    public static void main(String[] args) {
        // 测试用例
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
        System.out.println("节点0到4的异或值: " + lct.queryXor(0, 4)); // 应该是1^2^3^4^5 = 1
        
        // 断开边
        lct.cut(2, 3);
        System.out.println("断开边2-3后，0和4是否连通: " + lct.isConnected(0, 4)); // 应该是false
        
        // 重新连接
        lct.link(2, 3);
        System.out.println("重新连接后，0和4是否连通: " + lct.isConnected(0, 4)); // 应该是true
    }
}