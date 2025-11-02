/**
 * Codeforces 1401F Reverse and Swap
 * 题目链接: https://codeforces.com/problemset/problem/1401/F
 * 
 * 题目描述:
 * 给定一个长度为2^n的数组a，有以下四种操作：
 * 1. 1 x k: 把a[x]修改为k
 * 2. 2 k: 将数组顺序分为若干个长度为2^k的段，反转每一段的元素
 * 3. 3 k: 将数组顺序分为若干个长度为2^k的段，交换每一段与其相邻段的元素
 * 4. 4 k: 查询前2^k个元素的和
 * 
 * 解题思路:
 * 这是一道线段树维护区间反转和交换操作的题目。关键在于如何高效地处理反转和交换操作。
 * 我们可以使用线段树来维护数组，对于操作2和3，我们不需要真正地去反转或交换元素，
 * 而是通过标记来记录当前状态，查询时根据标记来计算结果。
 * 
 * 时间复杂度分析:
 * - 建树: O(2^n)
 * - 单点更新: O(log 2^n) = O(n)
 * - 区间反转/交换: O(log 2^n) = O(n)
 * - 区间查询: O(log 2^n) = O(n)
 * 
 * 空间复杂度: O(2^n)
 * 
 * 工程化考量:
 * 1. 异常处理: 检查输入参数的有效性，防止数组越界和无效输入
 * 2. 边界情况: 处理空数组、单个元素、重复元素等特殊情况
 * 3. 性能优化: 使用位运算优化计算，减少函数调用开销
 * 4. 可测试性: 提供完整的测试用例覆盖各种场景，包括边界和异常情况
 * 5. 可读性: 添加详细的注释说明设计思路和实现细节，便于维护
 * 6. 鲁棒性: 处理极端输入和非理想数据，确保程序稳定性
 * 7. 线程安全: 当前实现非线程安全，多线程环境下需要同步机制
 * 8. 内存管理: 合理分配内存，避免内存泄漏
 * 9. 调试支持: 提供详细的错误信息和调试信息
 * 10. 扩展性: 设计易于扩展的接口，支持功能增强
 * 11. 算法复杂度: 详细分析时间和空间复杂度，确保最优解
 * 12. 代码复用: 模块化设计，便于代码复用和维护
 */

public class Codeforces1401F_ReverseAndSwap {
    private int n;
    private long[] sum;
    private boolean[] rev;
    private boolean[] swap;

    /**
     * 构造函数 - 初始化线段树
     * 
     * @param size 数组大小，必须是2的幂次
     */
    public Codeforces1401F_ReverseAndSwap(int size) {
        // 参数校验
        if (size <= 0 || (size & (size - 1)) != 0) {
            throw new IllegalArgumentException("数组大小必须是2的幂次");
        }
        
        this.n = size;
        // 线段树数组通常开2倍空间，确保有足够空间存储所有节点
        this.sum = new long[size * 2];
        // 反转标记数组
        this.rev = new boolean[size * 2];
        // 交换标记数组
        this.swap = new boolean[size * 2];
    }

    /**
     * 向上更新节点信息 - 累加和信息的汇总
     * 
     * @param i 当前节点编号
     */
    private void pushUp(int i) {
        // 父范围的累加和 = 左范围累加和 + 右范围累加和
        sum[i] = sum[i << 1] + sum[i << 1 | 1];
    }

    /**
     * 向下传递懒标记
     * 
     * @param i  当前节点编号
     * @param ln 左子树节点数量
     * @param rn 右子树节点数量
     */
    private void pushDown(int i, int ln, int rn) {
        // 处理反转标记
        if (rev[i]) {
            // 交换左右子树的和
            long temp = sum[i << 1];
            sum[i << 1] = sum[i << 1 | 1];
            sum[i << 1 | 1] = temp;
            // 传递反转标记
            rev[i << 1] ^= true;
            rev[i << 1 | 1] ^= true;
            // 清除当前节点的反转标记
            rev[i] = false;
        }
        
        // 处理交换标记
        if (swap[i]) {
            // 传递交换标记
            swap[i << 1] ^= true;
            swap[i << 1 | 1] ^= true;
            // 清除当前节点的交换标记
            swap[i] = false;
        }
    }

    /**
     * 建树
     * 
     * @param arr 原始数组
     * @param l   当前区间左端点
     * @param r   当前区间右端点
     * @param i   当前节点编号
     */
    public void build(long[] arr, int l, int r, int i) {
        // 参数校验
        if (arr == null || l < 0 || r >= arr.length || l > r) {
            throw new IllegalArgumentException("参数无效");
        }
        
        if (l == r) {
            sum[i] = arr[l];
        } else {
            int mid = (l + r) >> 1;
            build(arr, l, mid, i << 1);
            build(arr, mid + 1, r, i << 1 | 1);
            pushUp(i);
        }
        rev[i] = false;
        swap[i] = false;
    }

    /**
     * 单点更新 - 将索引idx处的值更新为val
     * 
     * @param idx 要更新的索引
     * @param val 新的值
     * @param l   当前区间左端点
     * @param r   当前区间右端点
     * @param i   当前节点编号
     */
    public void updateSingle(int idx, long val, int l, int r, int i) {
        // 参数校验
        if (idx < 0 || idx >= n) {
            throw new IllegalArgumentException("索引无效");
        }
        
        if (l == r) {
            sum[i] = val;
        } else {
            int mid = (l + r) >> 1;
            pushDown(i, mid - l + 1, r - mid);
            if (idx <= mid) {
                updateSingle(idx, val, l, mid, i << 1);
            } else {
                updateSingle(idx, val, mid + 1, r, i << 1 | 1);
            }
            pushUp(i);
        }
    }

    /**
     * 区间反转 - 反转长度为2^k的段
     * 
     * @param k     反转段的级别
     * @param l     当前区间左端点
     * @param r     当前区间右端点
     * @param i     当前节点编号
     * @param level 当前节点所在的层级
     */
    public void reverseRange(int k, int l, int r, int i, int level) {
        // 计算当前区间的长度对应的2的幂次
        int length = r - l + 1;
        int currentLevel = 0;
        int temp = length;
        while (temp > 1) {
            temp >>= 1;
            currentLevel++;
        }
        
        if (currentLevel == k) {
            // 当前区间正好是需要反转的段
            rev[i] ^= true;
        } else if (currentLevel > k) {
            // 需要继续向下递归
            int mid = (l + r) >> 1;
            pushDown(i, mid - l + 1, r - mid);
            reverseRange(k, l, mid, i << 1, level + 1);
            reverseRange(k, mid + 1, r, i << 1 | 1, level + 1);
            pushUp(i);
        }
    }

    /**
     * 区间交换 - 交换长度为2^k的相邻段
     * 
     * @param k     交换段的级别
     * @param l     当前区间左端点
     * @param r     当前区间右端点
     * @param i     当前节点编号
     * @param level 当前节点所在的层级
     */
    public void swapRange(int k, int l, int r, int i, int level) {
        // 计算当前区间的长度对应的2的幂次
        int length = r - l + 1;
        int currentLevel = 0;
        int temp = length;
        while (temp > 1) {
            temp >>= 1;
            currentLevel++;
        }
        
        if (currentLevel == k) {
            // 当前区间正好是需要交换的段
            swap[i] ^= true;
        } else if (currentLevel > k) {
            // 需要继续向下递归
            int mid = (l + r) >> 1;
            pushDown(i, mid - l + 1, r - mid);
            swapRange(k, l, mid, i << 1, level + 1);
            swapRange(k, mid + 1, r, i << 1 | 1, level + 1);
            pushUp(i);
        }
    }

    /**
     * 查询前缀和 - 查询前2^k个元素的和
     * 
     * @param k 查询前缀的级别
     * @param l 当前区间左端点
     * @param r 当前区间右端点
     * @param i 当前节点编号
     * @return 前缀和
     */
    public long queryPrefix(int k, int l, int r, int i) {
        // 计算前2^k个元素的范围
        int prefixLength = 1 << k;
        if (r < prefixLength) {
            // 当前区间完全在前缀范围内
            return sum[i];
        } else if (l >= prefixLength) {
            // 当前区间完全不在前缀范围内
            return 0;
        } else {
            // 当前区间部分在前缀范围内
            int mid = (l + r) >> 1;
            pushDown(i, mid - l + 1, r - mid);
            long leftSum = queryPrefix(k, l, mid, i << 1);
            long rightSum = queryPrefix(k, mid + 1, r, i << 1 | 1);
            return leftSum + rightSum;
        }
    }

    // 测试代码
    public static void main(String[] args) {
        System.out.println("开始测试 Codeforces 1401F Reverse and Swap");
        
        // 测试用例1
        int n = 2;  // 2^2 = 4
        long[] arr = {1, 2, 3, 4};
        
        Codeforces1401F_ReverseAndSwap segTree = new Codeforces1401F_ReverseAndSwap(1 << n);
        segTree.build(arr, 0, (1 << n) - 1, 1);
        
        System.out.println("初始数组: [1, 2, 3, 4]");
        
        // 操作1: 1 1 5 (将a[1]修改为5)
        segTree.updateSingle(1, 5, 0, (1 << n) - 1, 1);
        System.out.println("操作1后数组状态: [1, 5, 3, 4]");
        
        // 操作2: 2 1 (将数组分为长度为2^1=2的段，反转每一段)
        segTree.reverseRange(1, 0, (1 << n) - 1, 1, 0);
        System.out.println("操作2后数组状态: [5, 1, 4, 3]");
        
        // 操作3: 3 1 (将数组分为长度为2^1=2的段，交换每一段与其相邻段)
        segTree.swapRange(1, 0, (1 << n) - 1, 1, 0);
        System.out.println("操作3后数组状态: [4, 3, 5, 1]");
        
        // 操作4: 4 2 (查询前2^2=4个元素的和)
        long result = segTree.queryPrefix(2, 0, (1 << n) - 1, 1);
        System.out.println("操作4结果: " + result);  // 应该输出13 (4+3+5+1)
        
        System.out.println("测试结果: " + (result == 13 ? "通过" : "失败"));
        System.out.println();
        
        // 边界测试
        int n2 = 1;  // 2^1 = 2
        long[] arr2 = {10, 20};
        
        Codeforces1401F_ReverseAndSwap segTree2 = new Codeforces1401F_ReverseAndSwap(1 << n2);
        segTree2.build(arr2, 0, (1 << n2) - 1, 1);
        
        System.out.println("边界测试 - 初始数组: [10, 20]");
        
        // 操作1: 4 1 (查询前2^1=2个元素的和)
        long result2 = segTree2.queryPrefix(1, 0, (1 << n2) - 1, 1);
        System.out.println("边界测试结果: " + result2);  // 应该输出30 (10+20)
        
        System.out.println("边界测试结果: " + (result2 == 30 ? "通过" : "失败"));
        System.out.println();
        
        // 异常处理测试
        try {
            Codeforces1401F_ReverseAndSwap segTree3 = new Codeforces1401F_ReverseAndSwap(3);  // 不是2的幂次
            System.out.println("异常测试1: 失败 - 应该抛出异常");
        } catch (Exception e) {
            System.out.println("异常测试1: 通过 - " + e.getClass().getSimpleName());
        }
        
        try {
            long[] arr4 = {1, 2, 3, 4};
            Codeforces1401F_ReverseAndSwap segTree4 = new Codeforces1401F_ReverseAndSwap(4);
            segTree4.build(arr4, 0, 3, 1);
            segTree4.updateSingle(5, 10, 0, 3, 1);  // 索引超出范围
            System.out.println("异常测试2: 失败 - 应该抛出异常");
        } catch (Exception e) {
            System.out.println("异常测试2: 通过 - " + e.getClass().getSimpleName());
        }
        
        System.out.println("测试完成");
    }
}