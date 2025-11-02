/**
 * 洛谷 P3919 【模板】可持久化数组（可持久化线段树/平衡树）
 * 题目链接: https://www.luogu.com.cn/problem/P3919
 *
 * 题目描述:
 * 如题，你需要维护这样的一个长度为N的数组，支持如下几种操作：
 * 1. 在某个历史版本上修改某一个位置上的值
 * 2. 访问某个历史版本上的某一个位置的值
 *
 * 输入格式:
 * 第一行包含两个正整数N,M，分别表示数组的长度和操作的个数。
 * 第二行包含N个整数，第i个整数表示初始状态下数组第i项的值。
 * 接下来M行每行包含若干个整数，表示一个操作。
 *
 * 操作格式如下：
 * 1. C i x v：在版本x的基础上，将位置i的值修改为v，并生成新版本
 * 2. Q i x：查询版本x中位置i的值
 *
 * 输出格式:
 * 对于每个Q操作，输出一行一个整数表示答案。
 *
 * 时间复杂度: 
 * - 建树: O(n)
 * - 单点修改: O(log n)
 * - 单点查询: O(log n)
 *
 * 空间复杂度: O(n log n)
 *
 * 解题思路:
 * 使用可持久化线段树（主席树）来解决这个问题：
 * 1. 可持久化线段树是一种可以保存历史版本的数据结构
 * 2. 每次修改时，只创建从根到修改节点路径上的新节点，其余节点共享
 * 3. 这样可以大大节省空间，同时保留所有历史版本
 * 4. 查询时，根据版本号找到对应的根节点，然后进行常规线段树查询
 */

import java.util.*;

public class LuoguP3919_PersistentSegmentTree {
    
    /**
     * 可持久化线段树节点
     */
    static class PersistentSegmentTreeNode {
        PersistentSegmentTreeNode left;   // 左子节点
        PersistentSegmentTreeNode right;  // 右子节点
        int val;                          // 节点值
        
        PersistentSegmentTreeNode() {
            this.left = null;
            this.right = null;
            this.val = 0;
        }
        
        PersistentSegmentTreeNode(PersistentSegmentTreeNode left, PersistentSegmentTreeNode right, int val) {
            this.left = left;
            this.right = right;
            this.val = val;
        }
    }
    
    /**
     * 可持久化线段树（主席树）
     */
    static class PersistentSegmentTree {
        private int n;                           // 数组大小
        private List<PersistentSegmentTreeNode> roots;  // 存储各个版本的根节点
        
        /**
         * 构造函数
         * @param size 数组大小
         */
        public PersistentSegmentTree(int size) {
            this.n = size;
            this.roots = new ArrayList<>();
        }
        
        /**
         * 构建初始版本的线段树
         * 
         * @param arr 初始数组
         * @param l 当前区间左端点
         * @param r 当前区间右端点
         * @return 构建完成的节点
         */
        public PersistentSegmentTreeNode build(int[] arr, int l, int r) {
            PersistentSegmentTreeNode node = new PersistentSegmentTreeNode();
            if (l == r) {
                node.val = arr[l];
            } else {
                int mid = (l + r) >> 1;
                node.left = build(arr, l, mid);
                node.right = build(arr, mid + 1, r);
            }
            return node;
        }
        
        /**
         * 在前一个版本的基础上更新指定位置的值，生成新版本
         * 
         * @param preRoot 前一个版本的根节点
         * @param idx 要更新的位置（0-indexed）
         * @param val 新的值
         * @param l 当前区间左端点
         * @param r 当前区间右端点
         * @return 新版本的根节点
         */
        public PersistentSegmentTreeNode update(PersistentSegmentTreeNode preRoot, int idx, int val, int l, int r) {
            PersistentSegmentTreeNode node = new PersistentSegmentTreeNode();
            if (l == r) {
                node.val = val;
            } else {
                int mid = (l + r) >> 1;
                // 根据更新位置决定哪一边需要新建节点
                if (idx <= mid) {
                    // 更新左子树，右子树可以复用
                    node.left = update(preRoot.left, idx, val, l, mid);
                    node.right = preRoot.right;
                } else {
                    // 更新右子树，左子树可以复用
                    node.left = preRoot.left;
                    node.right = update(preRoot.right, idx, val, mid + 1, r);
                }
            }
            return node;
        }
        
        /**
         * 查询指定版本中指定位置的值
         * 
         * @param root 版本的根节点
         * @param idx 要查询的位置（0-indexed）
         * @param l 当前区间左端点
         * @param r 当前区间右端点
         * @return 位置idx处的值
         */
        public int query(PersistentSegmentTreeNode root, int idx, int l, int r) {
            if (l == r) {
                return root.val;
            }
            int mid = (l + r) >> 1;
            if (idx <= mid) {
                return query(root.left, idx, l, mid);
            } else {
                return query(root.right, idx, mid + 1, r);
            }
        }
        
        /**
         * 添加根节点到版本列表
         * @param root 根节点
         */
        public void addRoot(PersistentSegmentTreeNode root) {
            roots.add(root);
        }
        
        /**
         * 获取指定版本的根节点
         * @param version 版本号
         * @return 根节点
         */
        public PersistentSegmentTreeNode getRoot(int version) {
            return roots.get(version);
        }
    }
    
    /**
     * 主函数，处理输入输出
     */
    public static void main(String[] args) {
        // 为了演示，我们使用示例输入
        // 实际使用时应该用: 
        // Scanner scanner = new Scanner(System.in);
        String[] lines = {
            "5 9",
            "5 4 3 2 1",
            "1 1 0 3",    // 在版本0的基础上，将位置1的值修改为3
            "2 1 1",      // 查询版本1中位置1的值
            "1 2 1 5",    // 在版本1的基础上，将位置2的值修改为5
            "1 3 2 4",    // 在版本2的基础上，将位置3的值修改为4
            "2 2 3",      // 查询版本3中位置2的值
            "2 3 3",      // 查询版本3中位置3的值
            "1 4 2 2",    // 在版本2的基础上，将位置4的值修改为2
            "2 3 4",      // 查询版本4中位置3的值
            "2 4 4"       // 查询版本4中位置4的值
        };
        
        // 解析输入
        String[] nm = lines[0].split(" ");
        int n = Integer.parseInt(nm[0]);
        int m = Integer.parseInt(nm[1]);
        
        String[] arrStr = lines[1].split(" ");
        int[] arr = new int[n];
        for (int i = 0; i < n; i++) {
            arr[i] = Integer.parseInt(arrStr[i]);
        }
        
        // 初始化可持久化线段树
        PersistentSegmentTree pst = new PersistentSegmentTree(n);
        
        // 构建初始版本（版本0）
        PersistentSegmentTreeNode root0 = pst.build(arr, 0, n - 1);
        pst.addRoot(root0);
        
        // 处理操作
        List<Integer> results = new ArrayList<>();
        for (int i = 2; i < 2 + m; i++) {
            String[] operation = lines[i].split(" ");
            if (operation[0].equals("1")) {
                // 修改操作：C i x v
                // 在版本x的基础上，将位置i的值修改为v
                int iPos = Integer.parseInt(operation[1]) - 1;  // 转换为0-indexed
                int xVer = Integer.parseInt(operation[2]);
                int vVal = Integer.parseInt(operation[3]);
                
                // 在版本xVer的基础上更新位置iPos的值为vVal
                PersistentSegmentTreeNode newRoot = pst.update(pst.getRoot(xVer), iPos, vVal, 0, n - 1);
                pst.addRoot(newRoot);
            } else {
                // 查询操作：Q i x
                // 查询版本x中位置i的值
                int iPos = Integer.parseInt(operation[1]) - 1;  // 转换为0-indexed
                int xVer = Integer.parseInt(operation[2]);
                
                // 查询版本xVer中位置iPos的值
                int result = pst.query(pst.getRoot(xVer), iPos, 0, n - 1);
                results.add(result);
            }
        }
        
        // 输出查询结果
        for (int result : results) {
            System.out.println(result);
        }
    }
}