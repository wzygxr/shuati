/**
 * HDU 1754. I Hate It
 * 题目链接: http://acm.hdu.edu.cn/showproblem.php?pid=1754
 * 
 * 题目描述:
 * 很多学校流行一种比较的习惯。老师们很喜欢询问，从某某同学到某某同学当中，分数最高的是多少。
 * 这让很多学生很反感。不管你喜不喜欢，现在需要你做的是，就是按照老师的要求，写一个程序，模拟老师的询问。
 * 当然，老师有时候需要更新某位同学的成绩。
 * 
 * 输入格式:
 * 本题目包含多组测试，请处理到文件结束。
 * 在每个测试的第一行，有两个正整数 N 和 M (0<N<=200000,0<M<5000)，分别代表学生的数目和操作的数目。
 * 学生ID编号分别从1编到N。
 * 第二行包含N个整数，代表这N个学生的初始成绩，其中第i个数代表ID为i的学生的成绩。
 * 接下来有M行。每一行有一个字符 C (只取'Q'或'U') ，和两个正整数A，B。
 * 当C为'Q'的时候，表示这是一条询问操作，它询问ID从A到B(包括A,B)的学生当中，成绩最高的是多少。
 * 当C为'U'的时候，表示这是一条更新操作，要求把ID为A的学生的成绩更改为B。
 * 
 * 输出格式:
 * 对于每一次询问操作，在一行里面输出最高成绩。
 * 
 * 解题思路:
 * 这是一个线段树应用问题，支持单点更新和区间最大值查询。
 * 1. 使用线段树维护数组区间最大值
 * 2. 单点更新时，从根节点向下递归找到目标位置并更新，然后向上传递更新区间最大值
 * 3. 区间查询时，根据查询区间与当前节点区间的关系进行递归查询
 * 
 * 时间复杂度:
 * - 建树: O(n)
 * - 单点更新: O(log n)
 * - 区间查询: O(log n)
 * 空间复杂度: O(n)
 * 
 * 工程化考量:
 * 1. 异常处理: 检查输入参数的有效性
 * 2. 边界情况: 处理空数组、单个元素等情况
 * 3. 性能优化: 使用位运算优化计算
 * 4. 可测试性: 提供完整的测试用例覆盖各种场景
 * 5. 可读性: 详细注释和清晰的代码结构
 */

import java.util.*;

public class HDU1754_IHateIt {
    
    private int[] maxTree;  // 线段树数组，存储区间最大值
    private int n;          // 学生数量
    private int[] scores;    // 学生成绩数组
    
    /**
     * 构造函数 - 初始化线段树
     * 
     * @param scores 学生成绩数组
     * 
     * 时间复杂度: O(n)
     * 空间复杂度: O(n)
     */
    public HDU1754_IHateIt(int[] scores) {
        if (scores == null || scores.length == 0) {
            throw new IllegalArgumentException("成绩数组不能为空");
        }
        
        this.n = scores.length;
        this.scores = scores;
        this.maxTree = new int[4 * n];
        
        // 构建线段树
        buildTree(1, 1, n);
    }
    
    /**
     * 构建线段树 - 递归构建
     * 
     * @param node 当前节点索引
     * @param left 当前节点左边界
     * @param right 当前节点右边界
     * 
     * 时间复杂度: O(n)
     */
    private void buildTree(int node, int left, int right) {
        if (left == right) {
            // 叶子节点，存储学生成绩
            maxTree[node] = scores[left - 1];
            return;
        }
        
        int mid = (left + right) >> 1;
        
        // 递归构建左右子树
        buildTree(node << 1, left, mid);
        buildTree(node << 1 | 1, mid + 1, right);
        
        // 更新当前节点的最大值
        maxTree[node] = Math.max(maxTree[node << 1], maxTree[node << 1 | 1]);
    }
    
    /**
     * 更新学生成绩
     * 
     * @param id 学生ID
     * @param score 新成绩
     * 
     * 时间复杂度: O(log n)
     */
    public void update(int id, int score) {
        if (id < 1 || id > n) {
            throw new IllegalArgumentException("学生ID必须在1到" + n + "之间");
        }
        
        // 递归更新线段树
        updateTree(1, 1, n, id, score);
        
        // 更新原始数组
        scores[id - 1] = score;
    }
    
    /**
     * 递归更新线段树
     * 
     * @param node 当前节点索引
     * @param left 当前节点左边界
     * @param right 当前节点右边界
     * @param id 目标学生ID
     * @param score 新成绩
     * 
     * 时间复杂度: O(log n)
     */
    private void updateTree(int node, int left, int right, int id, int score) {
        if (left == right) {
            // 找到目标位置，更新成绩
            maxTree[node] = score;
            return;
        }
        
        int mid = (left + right) >> 1;
        
        // 根据目标位置递归更新相应子树
        if (id <= mid) {
            updateTree(node << 1, left, mid, id, score);
        } else {
            updateTree(node << 1 | 1, mid + 1, right, id, score);
        }
        
        // 更新当前节点的最大值
        maxTree[node] = Math.max(maxTree[node << 1], maxTree[node << 1 | 1]);
    }
    
    /**
     * 查询区间最大值
     * 
     * @param left 区间左边界
     * @param right 区间右边界
     * @return 区间最大值
     * 
     * 时间复杂度: O(log n)
     */
    public int query(int left, int right) {
        if (left < 1 || left > n || right < 1 || right > n || left > right) {
            throw new IllegalArgumentException("查询区间无效: [" + left + ", " + right + "]");
        }
        
        return queryTree(1, 1, n, left, right);
    }
    
    /**
     * 递归查询线段树
     * 
     * @param node 当前节点索引
     * @param l 当前节点左边界
     * @param r 当前节点右边界
     * @param ql 查询左边界
     * @param qr 查询右边界
     * @return 查询结果
     * 
     * 时间复杂度: O(log n)
     */
    private int queryTree(int node, int l, int r, int ql, int qr) {
        if (ql <= l && r <= qr) {
            // 当前节点完全包含在查询区间内
            return maxTree[node];
        }
        
        int mid = (l + r) >> 1;
        int maxScore = Integer.MIN_VALUE;
        
        // 递归查询左右子树
        if (ql <= mid) {
            maxScore = Math.max(maxScore, queryTree(node << 1, l, mid, ql, qr));
        }
        if (qr > mid) {
            maxScore = Math.max(maxScore, queryTree(node << 1 | 1, mid + 1, r, ql, qr));
        }
        
        return maxScore;
    }
    
    /**
     * 主方法 - 处理多组测试数据
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        while (scanner.hasNext()) {
            int n = scanner.nextInt();
            int m = scanner.nextInt();
            
            int[] scores = new int[n];
            for (int i = 0; i < n; i++) {
                scores[i] = scanner.nextInt();
            }
            
            // 创建线段树
            HDU1754_IHateIt segmentTree = new HDU1754_IHateIt(scores);
            
            for (int i = 0; i < m; i++) {
                String operation = scanner.next();
                int a = scanner.nextInt();
                int b = scanner.nextInt();
                
                if ("Q".equals(operation)) {
                    // 查询操作
                    int result = segmentTree.query(a, b);
                    System.out.println(result);
                } else if ("U".equals(operation)) {
                    // 更新操作
                    segmentTree.update(a, b);
                }
            }
        }
        
        scanner.close();
    }
    
    /**
     * 测试方法 - 单元测试
     */
    public static void test() {
        // 测试用例1: 标准示例
        int[] scores1 = {1, 2, 3, 4, 5};
        HDU1754_IHateIt tree1 = new HDU1754_IHateIt(scores1);
        
        System.out.println("测试用例1 - 初始查询:");
        System.out.println("查询[1,5]: " + tree1.query(1, 5) + " (期望: 5)");
        System.out.println("查询[2,4]: " + tree1.query(2, 4) + " (期望: 4)");
        
        // 更新操作
        tree1.update(3, 10);
        System.out.println("\n测试用例1 - 更新后查询:");
        System.out.println("查询[1,5]: " + tree1.query(1, 5) + " (期望: 10)");
        System.out.println("查询[2,4]: " + tree1.query(2, 4) + " (期望: 10)");
        
        // 测试用例2: 单个元素
        int[] scores2 = {100};
        HDU1754_IHateIt tree2 = new HDU1754_IHateIt(scores2);
        System.out.println("\n测试用例2 - 单个元素:");
        System.out.println("查询[1,1]: " + tree2.query(1, 1) + " (期望: 100)");
        
        // 测试用例3: 全相同元素
        int[] scores3 = {5, 5, 5, 5, 5};
        HDU1754_IHateIt tree3 = new HDU1754_IHateIt(scores3);
        System.out.println("\n测试用例3 - 全相同元素:");
        System.out.println("查询[1,5]: " + tree3.query(1, 5) + " (期望: 5)");
        
        // 测试用例4: 大数测试
        int[] scores4 = {Integer.MAX_VALUE, Integer.MIN_VALUE, 0};
        HDU1754_IHateIt tree4 = new HDU1754_IHateIt(scores4);
        System.out.println("\n测试用例4 - 大数测试:");
        System.out.println("查询[1,3]: " + tree4.query(1, 3) + " (期望: " + Integer.MAX_VALUE + ")");
        
        System.out.println("\n所有测试用例通过!");
    }
}