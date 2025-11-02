package class170;

/**
 * LeetCode 315. 计算右侧小于当前元素的个数
 * 平台: LeetCode
 * 难度: 困难
 * 标签: CDQ分治, 分治, 树状数组, 离散化
 * 链接: https://leetcode.cn/problems/count-of-smaller-numbers-after-self/
 * 
 * 题目描述:
 * 给你一个整数数组 nums ，按要求返回一个新数组 counts 。数组 counts 有该性质：
 * counts[i] 的值是 nums[i] 右侧小于 nums[i] 的元素的数量。
 * 
 * 示例:
 * 输入: nums = [5,2,6,1]
 * 输出: [2,1,1,0]
 * 解释:
 * 5 的右侧有 2 个更小的元素 (2 和 1)
 * 2 的右侧有 1 个更小的元素 (1)
 * 6 的右侧有 1 个更小的元素 (1)
 * 1 的右侧有 0 个更小的元素
 * 
 * 解题思路:
 * 使用CDQ分治解决这个问题，将问题转化为三维偏序问题：
 * 1. 第一维：索引，表示元素在原数组中的位置
 * 2. 第二维：数值，表示元素的值
 * 3. 第三维：时间/操作类型，用于区分查询和更新操作
 * 
 * CDQ分治的核心思想是：将问题分解为左右两部分，递归处理每个子问题，
 * 然后计算左半部分对右半部分的影响，最后合并结果。
 * 
 * 具体实现步骤：
 * 1. 离散化处理：将原始数值映射到较小的连续整数范围，便于树状数组操作
 * 2. 构建操作序列：从右向左遍历数组，为每个元素创建插入操作和查询操作
 * 3. 排序操作序列：按元素值排序，值相同的查询操作优先于插入操作
 * 4. 执行CDQ分治：在分治过程中使用树状数组维护前缀和，高效计算查询结果
 * 
 * 时间复杂度分析：
 * - 离散化和排序操作：O(n log n)
 * - CDQ分治的递归树深度：O(log n)
 * - 每层合并操作（含树状数组操作）：O(n log n)
 * - 总体时间复杂度：O(n log²n)
 * 
 * 空间复杂度分析：
 * - 存储操作序列：O(n)
 * - 树状数组：O(n)
 * - 递归调用栈：O(log n)
 * - 总体空间复杂度：O(n)
 */

import java.util.*;

/**
 * 操作类：表示插入或查询操作
 * 用于构建CDQ分治所需的操作序列
 */
class Operation implements Comparable<Operation> {
    int op;  // 操作类型：1表示插入，-1表示查询
    int val; // 元素的原始值
    int idx; // 元素在原数组中的索引位置
    int id;  // 离散化后的ID，用于树状数组操作
    
    /**
     * 构造函数
     * @param op 操作类型
     * @param val 元素值
     * @param idx 原始索引
     * @param id 离散化后的ID
     */
    public Operation(int op, int val, int idx, int id) {
        this.op = op;
        this.val = val;
        this.idx = idx;
        this.id = id;
    }
    
    /**
     * 比较函数，定义操作序列的排序规则
     * 1. 首先按元素值从小到大排序
     * 2. 值相同时，查询操作(-1)优先于插入操作(1)，避免重复计数
     */
    @Override
    public int compareTo(Operation other) {
        if (this.val != other.val) {
            return Integer.compare(this.val, other.val);
        }
        // 查询操作优先于插入操作，确保在计算时已插入的元素不被重复计算
        return Integer.compare(other.op, this.op);
    }
}

/**
 * 解决方案类：实现CDQ分治算法
 */
class Solution {
    private int[] bit;  // 树状数组，用于维护前缀和
    
    /**
     * lowbit操作：计算x的最低位1所代表的值
     * @param x 输入整数
     * @return 最低位1代表的值
     */
    private int lowbit(int x) {
        return x & (-x); // 利用补码特性获取最低位的1
    }
    
    /**
     * 树状数组的单点更新操作
     * @param x 要更新的位置（离散化后的ID）
     * @param v 更新的增量值
     * @param n 树状数组的大小
     */
    private void add(int x, int v, int n) {
        // 向上更新所有相关节点
        for (int i = x; i <= n; i += lowbit(i)) {
            bit[i] += v;
        }
    }
    
    /**
     * 树状数组的前缀和查询操作
     * @param x 查询的上界位置
     * @return [1, x]区间的前缀和
     */
    private int query(int x) {
        int res = 0;
        // 向下累加所有包含信息的节点
        for (int i = x; i > 0; i -= lowbit(i)) {
            res += bit[i];
        }
        return res;
    }
    
    /**
     * 主函数：计算右侧小于当前元素的个数
     * @param nums 输入数组
     * @return 结果列表，每个元素表示对应位置右侧小于该元素的个数
     */
    public List<Integer> countSmaller(int[] nums) {
        int n = nums.length;
        // 边界条件处理：空数组
        if (n == 0) return new ArrayList<>();
        
        // 离散化处理：将原始数值映射到较小的连续整数范围
        int[] sortedNums = nums.clone(); // 复制原数组
        Arrays.sort(sortedNums);         // 排序
        int uniqueSize = removeDuplicates(sortedNums); // 去重，得到唯一值的数量
        
        // 初始化操作序列、结果数组和树状数组
        Operation[] ops = new Operation[2 * n]; // 每个元素对应两个操作
        int[] result = new int[n];              // 存储结果
        bit = new int[uniqueSize + 1];          // 树状数组大小为唯一值的数量+1
        
        int cnt = 0;
        // 从右向左处理，构造操作序列
        // 这样可以确保在处理元素i时，所有右侧元素已经被处理
        for (int i = n - 1; i >= 0; i--) {
            // 获取当前元素的离散化ID，+1是因为树状数组从1开始索引
            int valId = Arrays.binarySearch(sortedNums, 0, uniqueSize, nums[i]) + 1;
            if (valId < 0) {
                valId = -valId; // 处理未找到的情况（实际不应该发生，因为我们是对原数组排序去重得到的sortedNums）
            }
            
            // 插入操作：将当前元素插入到树状数组中
            ops[cnt++] = new Operation(1, nums[i], i, valId);
            
            // 查询操作：查询小于当前元素值的已插入元素个数
            // 注意这里使用的是valId-1，因为我们要找严格小于当前值的元素
            ops[cnt++] = new Operation(-1, nums[i] - 1, i, valId);
        }
        
        // 按值排序操作序列，确保较小的值先被处理
        Arrays.sort(ops, 0, cnt);
        
        // 执行CDQ分治，计算每个查询操作的结果
        cdq(ops, result, 0, cnt - 1, uniqueSize);
        
        // 构造最终结果列表
        List<Integer> res = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            res.add(result[i]);
        }
        return res;
    }
    
    /**
     * CDQ分治主函数
     * @param ops 操作序列
     * @param result 结果数组
     * @param l 当前处理区间的左边界
     * @param r 当前处理区间的右边界
     * @param n 树状数组的大小
     */
    private void cdq(Operation[] ops, int[] result, int l, int r, int n) {
        // 递归终止条件：区间长度为1或0
        if (l >= r) return;
        
        // 划分子区间，(l + r) >> 1 等价于 (l + r) / 2
        int mid = (l + r) >> 1;
        
        // 递归处理左半部分
        cdq(ops, result, l, mid, n);
        // 递归处理右半部分
        cdq(ops, result, mid + 1, r, n);
        
        // 合并阶段：计算左半部分对右半部分的贡献
        // 使用双指针法合并两个有序子数组
        Operation[] tmp = new Operation[r - l + 1]; // 临时数组，用于保存合并结果
        int i = l;      // 左半部分指针
        int j = mid + 1; // 右半部分指针
        int k = 0;      // 临时数组指针
        
        // 双指针遍历左右子区间
        while (i <= mid && j <= r) {
            if (ops[i].idx <= ops[j].idx) {
                // 左半部分的元素在原数组中的位置先于右半部分
                // 对于插入操作，更新树状数组
                if (ops[i].op == 1) {
                    add(ops[i].id, 1, n);  // 插入元素到树状数组
                }
                tmp[k++] = ops[i++];
            } else {
                // 右半部分的元素在原数组中的位置先于左半部分
                // 对于查询操作，计算树状数组中的前缀和
                if (ops[j].op == -1) {
                    // 查询严格小于当前值的元素个数，所以使用id-1
                    result[ops[j].idx] += query(ops[j].id - 1);
                }
                tmp[k++] = ops[j++];
            }
        }
        
        // 处理左半部分剩余的元素
        while (i <= mid) {
            tmp[k++] = ops[i++];
        }
        
        // 处理右半部分剩余的元素
        while (j <= r) {
            if (ops[j].op == -1) {
                // 对剩余的查询操作继续处理
                result[ops[j].idx] += query(ops[j].id - 1);
            }
            tmp[k++] = ops[j++];
        }
        
        // 清理树状数组：撤销左半部分插入操作的影响
        // 这一步至关重要，确保不会影响后续区间的处理
        for (int t = l; t <= mid; t++) {
            if (ops[t].op == 1) {
                add(ops[t].id, -1, n); // 减去之前添加的值
            }
        }
        
        // 将临时数组中的结果复制回原操作序列
        // 保证操作序列在当前区间内按索引有序
        for (int t = 0; t < k; t++) {
            ops[l + t] = tmp[t];
        }
    }
    
    /**
     * 数组去重函数
     * 原地去重，返回去重后的数组长度
     * @param nums 已排序的数组
     * @return 去重后的数组长度
     */
    private int removeDuplicates(int[] nums) {
        if (nums.length == 0) return 0;
        int uniqueSize = 1; // 非重复元素的个数，至少为1
        // 遍历数组，将不重复的元素移到前面
        for (int i = 1; i < nums.length; i++) {
            if (nums[i] != nums[uniqueSize - 1]) {
                nums[uniqueSize++] = nums[i];
            }
        }
        return uniqueSize;
    }
    
    /**
     * 主函数：测试代码
     */
    public static void main(String[] args) {
        Solution solution = new Solution();
        
        // 测试用例1：基本用例
        int[] nums1 = {5, 2, 6, 1};
        List<Integer> result1 = solution.countSmaller(nums1);
        
        System.out.println("测试用例1:");
        System.out.println("输入: " + java.util.Arrays.toString(nums1));
        System.out.println("输出: " + result1);
        System.out.println("期望输出: [2, 1, 1, 0]");
        System.out.println();
        
        // 测试用例2：空数组
        int[] nums2 = {};
        List<Integer> result2 = solution.countSmaller(nums2);
        System.out.println("测试用例2:");
        System.out.println("输入: " + java.util.Arrays.toString(nums2));
        System.out.println("输出: " + result2);
        System.out.println("期望输出: []");
        System.out.println();
        
        // 测试用例3：全相同元素
        int[] nums3 = {3, 3, 3, 3};
        List<Integer> result3 = solution.countSmaller(nums3);
        System.out.println("测试用例3:");
        System.out.println("输入: " + java.util.Arrays.toString(nums3));
        System.out.println("输出: " + result3);
        System.out.println("期望输出: [3, 2, 1, 0]");
        System.out.println();
        
        // 测试用例4：逆序数组
        int[] nums4 = {4, 3, 2, 1};
        List<Integer> result4 = solution.countSmaller(nums4);
        System.out.println("测试用例4:");
        System.out.println("输入: " + java.util.Arrays.toString(nums4));
        System.out.println("输出: " + result4);
        System.out.println("期望输出: [3, 2, 1, 0]");
        System.out.println();
        
        // 测试用例5：升序数组
        int[] nums5 = {1, 2, 3, 4};
        List<Integer> result5 = solution.countSmaller(nums5);
        System.out.println("测试用例5:");
        System.out.println("输入: " + java.util.Arrays.toString(nums5));
        System.out.println("输出: " + result5);
        System.out.println("期望输出: [0, 0, 0, 0]");
    }
}

/**
 * 算法分析与工程化思考：
 * 
 * 1. 最优解分析：
 *    - 此实现使用CDQ分治结合树状数组，时间复杂度为O(n log²n)，是该问题的最优解之一
 *    - 其他可能的解法包括归并排序（逆序对统计）、二叉搜索树等，但CDQ分治在处理多维偏序问题上更具通用性
 *    - 相比树套树等高级数据结构，CDQ分治实现更简洁，常数因子更小
 * 
 * 2. 性能优化：
 *    - 离散化处理是关键优化，有效降低了数值范围，节省内存并提高缓存命中率
 *    - 树状数组的update和query操作都是O(log n)时间复杂度，效率高
 *    - 从右向左遍历构造操作序列，简化了问题处理逻辑
 * 
 * 3. 语言特性考量：
 *    - Java中，数组操作比集合更高效，因此在关键路径上使用数组而非ArrayList
 *    - 使用Arrays.binarySearch进行二分查找，比手动实现更高效且不易出错
 *    - 位移操作 (l + r) >> 1 比直接相除 (l + r) / 2 更高效
 * 
 * 4. 边界情况处理：
 *    - 空数组：直接返回空列表
 *    - 数组元素全相同：正确计算每个元素右侧的元素个数
 *    - 极值数据：由于使用离散化，即使数值范围很大也能高效处理
 * 
 * 5. CDQ分治的适用场景：
 *    - 多维偏序问题
 *    - 离线查询问题
 *    - 需要将动态问题转化为静态问题处理的场景
 *    - 各种统计类问题，如逆序对、区间查询等
 * 
 * 6. 工程化改进建议：
 *    - 添加输入参数验证，处理null输入等异常情况
 *    - 增加日志记录，便于调试和性能监控
 *    - 对大规模数据，考虑并行化处理或优化内存使用
 *    - 添加单元测试，覆盖更多边界情况和特殊输入
 * 
 * 7. 与机器学习的联系：
 *    - CDQ分治的思想在某些机器学习算法中也有应用，如并行化处理大规模数据
 *    - 离散化技术在特征工程中常用，如将连续特征转换为离散特征
 *    - 前缀和计算在数据分析和信号处理中广泛使用
 */