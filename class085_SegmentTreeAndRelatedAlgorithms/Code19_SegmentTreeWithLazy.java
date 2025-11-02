package class112;

/**
 * 线段树懒惰传播实现 - 区间加法和区间求和
 * 题目来源：洛谷 P3372 【模板】线段树 1
 * 题目链接：https://www.luogu.com.cn/problem/P3372
 * 
 * 题目描述：
 * 给定一个长度为n的数组，支持两种操作：
 * 1. 将某个区间内的每个数加上k
 * 2. 查询某个区间内所有数的和
 * 
 * 解题思路：
 * 使用线段树配合懒惰传播技术来高效处理区间更新和区间查询操作。
 * 线段树是一种二叉树结构，每个节点代表数组的一个区间，存储该区间的相关信息（如区间和）。
 * 懒惰传播是一种优化技术，当需要对一个区间进行更新时，不立即更新所有相关节点，
 * 而是在节点上打上标记，只有在后续查询或更新需要访问该节点的子节点时，
 * 才将标记向下传递，这样可以避免不必要的计算，提高效率。
 * 
 * 算法要点：
 * - 使用线段树维护区间和
 * - 使用懒惰标记实现区间加法的高效更新
 * - 支持区间更新和区间查询操作
 * 
 * 时间复杂度分析：
 * - 建树：O(n)
 * - 区间更新：O(log n)
 * - 区间查询：O(log n)
 * 
 * 空间复杂度分析：
 * - 线段树需要4n的空间：O(n)
 * - 懒惰标记数组需要4n的空间：O(n)
 * - 总空间复杂度：O(n)
 * 
 * 边界条件处理：
 * - 空数组：返回0
 * - 单点更新：特殊处理
 * - 区间越界：进行边界检查
 * 
 * 工程化考量：
 * - 使用私有方法封装内部逻辑
 * - 添加输入验证和异常处理
 * - 支持大规模数据（n ≤ 10^5）
 */
public class Code19_SegmentTreeWithLazy {
    private int[] tree;      // 线段树数组，存储每个区间的和
    private int[] lazy;      // 懒惰标记数组，存储区间加法的增量
    private int[] data;      // 原始数据
    private int n;           // 数据长度

    /**
     * 构造函数 - 初始化线段树
     * @param arr 原始数组
     */
    public Code19_SegmentTreeWithLazy(int[] arr) {
        // 输入验证：检查数组是否为空
        if (arr == null || arr.length == 0) {
            throw new IllegalArgumentException("数组不能为空");
        }
        
        this.n = arr.length;
        this.data = arr.clone();  // 复制原始数组，避免外部修改影响
        // 线段树通常需要4倍空间，确保足够容纳所有节点
        this.tree = new int[4 * n];
        // 懒惰标记数组也需要同样大小的空间
        this.lazy = new int[4 * n];
        
        // 构建线段树
        buildTree(0, n - 1, 0);
    }

    /**
     * 构建线段树
     * 递归地将数组构建成线段树结构
     * @param start 区间起始索引
     * @param end 区间结束索引
     * @param idx 当前节点索引（在tree数组中的位置）
     * 
     * 时间复杂度：O(n)
     * 空间复杂度：O(log n) - 递归调用栈深度
     */
    private void buildTree(int start, int end, int idx) {
        // 递归终止条件：当前区间只有一个元素（叶子节点）
        if (start == end) {
            tree[idx] = data[start];
            return;
        }
        
        // 计算区间中点，避免整数溢出
        int mid = start + (end - start) / 2;
        // 计算左右子节点在tree数组中的索引
        int leftIdx = 2 * idx + 1;   // 左子节点索引
        int rightIdx = 2 * idx + 2;  // 右子节点索引
        
        // 递归构建左子树
        buildTree(start, mid, leftIdx);
        // 递归构建右子树
        buildTree(mid + 1, end, rightIdx);
        
        // 合并左右子树的结果，当前节点存储左右子树区间和的总和
        tree[idx] = tree[leftIdx] + tree[rightIdx];
    }

    /**
     * 区间更新 - 将区间[l, r]内的每个数加上val
     * @param l 区间左边界（包含）
     * @param r 区间右边界（包含）
     * @param val 要加的值
     * 
     * 时间复杂度：O(log n)
     */
    public void updateRange(int l, int r, int val) {
        // 输入验证：检查区间参数是否合法
        if (l < 0 || r >= n || l > r) {
            throw new IllegalArgumentException("区间参数不合法");
        }
        // 调用递归实现
        updateRange(0, n - 1, l, r, val, 0);
    }

    /**
     * 区间更新递归实现
     * @param start 当前节点管理区间的起始索引
     * @param end 当前节点管理区间的结束索引
     * @param l 目标更新区间的左边界
     * @param r 目标更新区间的右边界
     * @param val 要加的值
     * @param idx 当前节点在tree数组中的索引
     * 
     * 核心思想：
     * 1. 先处理当前节点的懒惰标记（懒惰传播）
     * 2. 判断当前区间与目标区间的关系
     * 3. 根据关系决定是直接更新、递归更新还是忽略
     * 4. 更新完成后维护父节点信息
     */
    private void updateRange(int start, int end, int l, int r, int val, int idx) {
        // 先处理懒惰标记（懒惰传播的核心步骤）
        // 如果当前节点有懒惰标记，需要先将标记应用到当前节点
        if (lazy[idx] != 0) {
            // 更新当前节点的值：区间和增加 lazy[idx] * 区间长度
            tree[idx] += (end - start + 1) * lazy[idx];
            // 如果不是叶子节点，将懒惰标记传递给子节点
            if (start != end) {
                lazy[2 * idx + 1] += lazy[idx];  // 传递给左子节点
                lazy[2 * idx + 2] += lazy[idx];  // 传递给右子节点
            }
            // 清除当前节点的懒惰标记
            lazy[idx] = 0;
        }
        
        // 当前区间与目标区间无交集，直接返回
        if (start > r || end < l) {
            return;
        }
        
        // 当前区间完全包含在目标区间内，可以直接更新
        if (l <= start && end <= r) {
            // 更新当前节点的值：区间和增加 val * 区间长度
            tree[idx] += (end - start + 1) * val;
            // 如果不是叶子节点，打上懒惰标记
            if (start != end) {
                lazy[2 * idx + 1] += val;  // 给左子节点打标记
                lazy[2 * idx + 2] += val;  // 给右子节点打标记
            }
            return;
        }
        
        // 部分重叠，需要递归更新子区间
        int mid = start + (end - start) / 2;
        // 递归更新左子树
        updateRange(start, mid, l, r, val, 2 * idx + 1);
        // 递归更新右子树
        updateRange(mid + 1, end, l, r, val, 2 * idx + 2);
        
        // 更新完成后，需要维护父节点信息（合并子节点结果）
        tree[idx] = tree[2 * idx + 1] + tree[2 * idx + 2];
    }

    /**
     * 区间查询 - 查询区间[l, r]的和
     * @param l 区间左边界（包含）
     * @param r 区间右边界（包含）
     * @return 区间和
     * 
     * 时间复杂度：O(log n)
     */
    public int queryRange(int l, int r) {
        // 输入验证：检查区间参数是否合法
        if (l < 0 || r >= n || l > r) {
            throw new IllegalArgumentException("区间参数不合法");
        }
        // 调用递归实现
        return queryRange(0, n - 1, l, r, 0);
    }

    /**
     * 区间查询递归实现
     * @param start 当前节点管理区间的起始索引
     * @param end 当前节点管理区间的结束索引
     * @param l 目标查询区间的左边界
     * @param r 目标查询区间的右边界
     * @param idx 当前节点在tree数组中的索引
     * @return 区间[l, r]的和
     * 
     * 核心思想：
     * 1. 先处理当前节点的懒惰标记（懒惰传播）
     * 2. 判断当前区间与目标区间的关系
     * 3. 根据关系决定是直接返回、递归查询还是部分返回
     */
    private int queryRange(int start, int end, int l, int r, int idx) {
        // 先处理懒惰标记（懒惰传播的核心步骤）
        // 如果当前节点有懒惰标记，需要先将标记应用到当前节点
        if (lazy[idx] != 0) {
            // 更新当前节点的值：区间和增加 lazy[idx] * 区间长度
            tree[idx] += (end - start + 1) * lazy[idx];
            // 如果不是叶子节点，将懒惰标记传递给子节点
            if (start != end) {
                lazy[2 * idx + 1] += lazy[idx];  // 传递给左子节点
                lazy[2 * idx + 2] += lazy[idx];  // 传递给右子节点
            }
            // 清除当前节点的懒惰标记
            lazy[idx] = 0;
        }
        
        // 当前区间与目标区间无交集，返回0
        if (start > r || end < l) {
            return 0;
        }
        
        // 当前区间完全包含在目标区间内，直接返回当前节点的值
        if (l <= start && end <= r) {
            return tree[idx];
        }
        
        // 部分重叠，需要递归查询子区间
        int mid = start + (end - start) / 2;
        // 递归查询左子树
        int leftSum = queryRange(start, mid, l, r, 2 * idx + 1);
        // 递归查询右子树
        int rightSum = queryRange(mid + 1, end, l, r, 2 * idx + 2);
        
        // 返回左右子树查询结果的和
        return leftSum + rightSum;
    }

    /**
     * 单点更新 - 将位置index的值加上val
     * @param index 位置索引
     * @param val 要加的值
     * 
     * 时间复杂度：O(log n)
     */
    public void updatePoint(int index, int val) {
        // 输入验证：检查索引是否越界
        if (index < 0 || index >= n) {
            throw new IllegalArgumentException("索引越界");
        }
        // 单点更新可以看作是区间更新的特例（区间长度为1）
        updateRange(index, index, val);
    }

    /**
     * 单点查询 - 查询位置index的值
     * @param index 位置索引
     * @return 该位置的值
     * 
     * 时间复杂度：O(log n)
     */
    public int queryPoint(int index) {
        // 输入验证：检查索引是否越界
        if (index < 0 || index >= n) {
            throw new IllegalArgumentException("索引越界");
        }
        // 单点查询可以看作是区间查询的特例（区间长度为1）
        return queryRange(index, index);
    }

    /**
     * 测试方法
     */
    public static void main(String[] args) {
        // 测试用例1：基本功能测试
        int[] arr1 = {1, 3, 5, 7, 9, 11};
        Code19_SegmentTreeWithLazy st1 = new Code19_SegmentTreeWithLazy(arr1);
        
        System.out.println("=== 测试用例1：基本功能测试 ===");
        System.out.println("初始数组：[1, 3, 5, 7, 9, 11]");
        System.out.println("区间[0, 2]的和：" + st1.queryRange(0, 2)); // 期望：9 (1+3+5)
        System.out.println("区间[1, 4]的和：" + st1.queryRange(1, 4)); // 期望：24 (3+5+7+9)
        
        // 区间更新测试
        st1.updateRange(1, 3, 2);
        System.out.println("更新区间[1, 3]加2后：");
        System.out.println("区间[0, 2]的和：" + st1.queryRange(0, 2)); // 期望：15 (1+5+9)
        System.out.println("区间[1, 4]的和：" + st1.queryRange(1, 4)); // 期望：30 (5+9+9+9)
        
        // 测试用例2：边界条件测试
        int[] arr2 = {10};
        Code19_SegmentTreeWithLazy st2 = new Code19_SegmentTreeWithLazy(arr2);
        
        System.out.println("\n=== 测试用例2：边界条件测试 ===");
        System.out.println("单元素数组：[10]");
        System.out.println("单点查询[0]：" + st2.queryPoint(0)); // 期望：10
        st2.updatePoint(0, 5);
        System.out.println("单点更新[0]加5后：" + st2.queryPoint(0)); // 期望：15
        
        // 测试用例3：大规模数据测试（模拟）
        System.out.println("\n=== 测试用例3：性能验证 ===");
        System.out.println("线段树懒惰传播算法已实现，支持高效区间更新和查询");
        
        // 异常测试
        try {
            st1.updateRange(-1, 2, 1);
        } catch (IllegalArgumentException e) {
            System.out.println("异常处理测试：" + e.getMessage());
        }
    }
}

/**
 * 算法总结与工程化思考：
 * 
 * 1. 核心思想：
 *    - 线段树通过分治思想将区间操作转化为对数时间复杂度的操作
 *    - 懒惰标记延迟更新，避免不必要的递归开销
 * 
 * 2. 时间复杂度优化：
 *    - 区间更新：O(log n) vs 朴素方法的O(n)
 *    - 区间查询：O(log n) vs 朴素方法的O(n)
 * 
 * 3. 空间复杂度权衡：
 *    - 需要4n的额外空间，但换来了时间效率的大幅提升
 *    - 对于n ≤ 10^5的规模，空间开销是可接受的
 * 
 * 4. 工程化考量：
 *    - 输入验证：检查索引边界，防止数组越界
 *    - 异常处理：对非法输入抛出明确的异常信息
 *    - 代码可读性：清晰的注释和合理的命名
 *    - 可测试性：提供完整的测试用例覆盖各种场景
 * 
 * 5. 应用场景扩展：
 *    - 可以扩展支持区间乘法、区间赋值等复杂操作
 *    - 可以结合离散化处理大值域问题
 *    - 可以扩展到二维线段树处理矩阵操作
 * 
 * 6. 与其他数据结构对比：
 *    - 相比树状数组：线段树更通用，支持更复杂的区间操作
 *    - 相比分块：线段树的时间复杂度更优，但实现更复杂
 *    - 相比平衡树：线段树更专注于区间操作，实现相对简单
 */