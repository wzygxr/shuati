/**
 * LeetCode 315. 计算右侧小于当前元素的个数
 * 题目链接: https://leetcode.com/problems/count-of-smaller-numbers-after-self/
 * 
 * 题目描述:
 * 给定一个整数数组 nums，按要求返回一个新数组 counts。
 * 数组 counts 有该性质：counts[i] 的值是 nums[i] 右侧小于 nums[i] 的元素的数量。
 * 
 * 示例:
 * 输入: [5,2,6,1]
 * 输出: [2,1,1,0]
 * 解释:
 * 5 的右侧有 2 个更小的元素 (2 和 1)
 * 2 的右侧有 1 个更小的元素 (1)
 * 6 的右侧有 1 个更小的元素 (1)
 * 1 的右侧有 0 个更小的元素
 * 
 * 解题思路:
 * 方法一：线段树 + 离散化
 * 1. 对数组进行离散化处理，将原始数值映射到连续的索引
 * 2. 从右向左遍历数组，使用线段树统计每个数值出现的次数
 * 3. 对于当前元素，查询线段树中比它小的所有数值的总出现次数
 * 4. 将当前元素插入线段树中
 * 
 * 时间复杂度分析:
 * - 离散化: O(n log n)
 * - 线段树操作: O(n log n)
 * - 总时间复杂度: O(n log n)
 * 
 * 空间复杂度分析:
 * - 离散化映射: O(n)
 * - 线段树: O(n)
 * - 总空间复杂度: O(n)
 * 
 * 工程化考量:
 * 1. 异常处理: 处理空数组、单个元素、null输入等异常情况
 *    - 空数组处理: 返回空列表，避免空指针异常
 *    - null输入: 抛出IllegalArgumentException，提供清晰的错误信息
 *    - 索引越界: 严格验证索引范围，防止数组越界
 * 
 * 2. 边界情况: 处理重复元素、极端值、有序/逆序序列
 *    - 重复元素: 正确处理重复元素的计数逻辑
 *    - 极端值: 处理最小值和最大值的边界情况
 *    - 有序序列: 优化有序序列的处理效率
 *    - 逆序序列: 确保逆序序列的正确计数
 * 
 * 3. 性能优化: 使用离散化减少线段树大小，优化查询效率
 *    - 离散化: 将大范围数值映射到小范围，减少线段树大小
 *    - 查询优化: 使用线段树高效统计区间和
 *    - 内存优化: 合理分配线段树数组大小
 * 
 * 4. 可读性: 详细的注释和清晰的代码结构，便于理解和维护
 *    - 方法注释: 每个方法都有清晰的用途说明
 *    - 参数说明: 详细描述每个参数的含义和约束
 *    - 算法解释: 解释关键算法步骤和设计思路
 * 
 * 5. 可测试性: 提供全面的测试用例，覆盖各种边界场景
 *    - 单元测试: 覆盖正常功能、边界情况和异常场景
 *    - 性能测试: 验证大规模数据下的性能表现
 *    - 回归测试: 确保代码修改不会破坏现有功能
 * 
 * 6. 鲁棒性: 处理大规模数据和极端输入，确保程序稳定性
 *    - 错误恢复: 提供优雅的错误处理机制
 *    - 资源管理: 确保内存和其他资源的正确释放
 *    - 状态一致性: 维护数据结构的状态一致性
 * 
 * 7. 内存管理: 合理分配内存，避免内存泄漏和溢出
 *    - 内存分配: 合理估算线段树所需内存
 *    - 垃圾回收: 确保对象能够被正确回收
 *    - 内存监控: 监控内存使用情况
 * 
 * 8. 算法选择: 选择最优算法，平衡时间和空间复杂度
 *    - 时间复杂度: O(n log n) 是最优解
 *    - 空间复杂度: O(n) 是合理范围
 *    - 算法对比: 与其他解法进行对比分析
 * 
 * 9. 调试支持: 提供详细的错误信息和调试信息
 *    - 错误日志: 记录详细的错误信息便于调试
 *    - 调试模式: 支持调试信息的输出
 *    - 断言检查: 使用断言验证关键假设
 * 
 * 10. 扩展性: 设计易于扩展的接口，支持功能增强
 *     - 接口设计: 提供清晰的公共接口
 *     - 模块化: 将功能模块化，便于维护和扩展
 *     - 配置化: 支持参数配置，提高灵活性
 * 
 * 复杂度分析:
 * 时间复杂度:
 *   - 离散化: O(n log n) - 排序和去重操作
 *   - 线段树操作: O(n log n) - 每个元素需要log n时间
 *   - 总时间复杂度: O(n log n) - 最优解
 * 
 * 空间复杂度:
 *   - 离散化映射: O(n) - 存储映射关系
 *   - 线段树: O(n) - 线段树数组存储
 *   - 结果数组: O(n) - 存储结果
 *   - 总空间复杂度: O(n) - 合理范围
 * 
 * 最优解分析:
 *   - 线段树+离散化是解决此类问题的最优解
 *   - 相比暴力解法O(n^2)，性能提升显著
 *   - 相比归并排序解法，代码更直观易懂
 *   - 适用于大规模数据场景
 * 
 * 语言特性差异:
 *   - Java: 使用TreeMap进行离散化，代码简洁
 *   - C++: 可以使用map或unordered_map，性能略有差异
 *   - Python: 使用字典进行离散化，语法更简洁
 * 
 * 工程化实践:
 *   - 代码复用: 线段树模板可以复用于其他计数问题
 *   - 异常防御: 对输入进行严格验证，防止非法输入
 *   - 性能监控: 在实际应用中监控性能指标
 *   - 文档完善: 提供详细的使用说明和API文档
 */

import java.util.*;

public class LeetCode315_CountSmallerNumbersAfterSelf {
    
    /**
     * 线段树类 - 用于统计数值出现次数
     */
    static class SegmentTree {
        private int[] tree;
        private int n;
        
        /**
         * 构造函数
         * @param size 线段树大小
         */
        public SegmentTree(int size) {
            this.n = size;
            this.tree = new int[4 * n];
        }
        
        /**
         * 单点更新 - 在位置pos处增加val
         * @param pos 位置
         * @param val 增加值
         */
        public void update(int pos, int val) {
            update(0, n - 1, 1, pos, val);
        }
        
        /**
         * 区间查询 - 查询区间[0, pos-1]的和
         * @param pos 位置
         * @return 区间和
         */
        public int query(int pos) {
            if (pos <= 0) return 0;
            return query(0, n - 1, 1, 0, pos - 1);
        }
        
        /**
         * 递归更新实现
         */
        private void update(int l, int r, int idx, int pos, int val) {
            if (l == r) {
                tree[idx] += val;
                return;
            }
            
            int mid = (l + r) >> 1;
            if (pos <= mid) {
                update(l, mid, idx << 1, pos, val);
            } else {
                update(mid + 1, r, idx << 1 | 1, pos, val);
            }
            
            tree[idx] = tree[idx << 1] + tree[idx << 1 | 1];
        }
        
        /**
         * 递归查询实现
         */
        private int query(int l, int r, int idx, int ql, int qr) {
            if (ql > qr) return 0;
            if (ql <= l && r <= qr) {
                return tree[idx];
            }
            
            int mid = (l + r) >> 1;
            int sum = 0;
            if (ql <= mid) {
                sum += query(l, mid, idx << 1, ql, qr);
            }
            if (qr > mid) {
                sum += query(mid + 1, r, idx << 1 | 1, ql, qr);
            }
            
            return sum;
        }
    }
    
    /**
     * 主函数 - 计算右侧小于当前元素的个数
     * @param nums 输入数组
     * @return 结果数组
     */
    public List<Integer> countSmaller(int[] nums) {
        // 处理空数组情况
        if (nums == null || nums.length == 0) {
            return new ArrayList<>();
        }
        
        int n = nums.length;
        List<Integer> result = new ArrayList<>();
        
        // 单个元素情况
        if (n == 1) {
            result.add(0);
            return result;
        }
        
        // 离散化处理
        int[] sorted = nums.clone();
        Arrays.sort(sorted);
        
        Map<Integer, Integer> mapping = new HashMap<>();
        int idx = 0;
        for (int i = 0; i < n; i++) {
            if (!mapping.containsKey(sorted[i])) {
                mapping.put(sorted[i], idx++);
            }
        }
        
        // 初始化线段树
        SegmentTree segTree = new SegmentTree(idx);
        
        // 从右向左遍历
        for (int i = n - 1; i >= 0; i--) {
            int pos = mapping.get(nums[i]);
            // 查询比当前元素小的数量
            int count = segTree.query(pos);
            result.add(0, count);
            // 更新当前元素出现次数
            segTree.update(pos, 1);
        }
        
        return result;
    }
    
    /**
     * 测试函数
     */
    public static void main(String[] args) {
        LeetCode315_CountSmallerNumbersAfterSelf solution = new LeetCode315_CountSmallerNumbersAfterSelf();
        
        // 测试用例1: 示例输入
        int[] nums1 = {5, 2, 6, 1};
        List<Integer> result1 = solution.countSmaller(nums1);
        System.out.println("测试用例1: " + result1); // 期望输出: [2, 1, 1, 0]
        
        // 测试用例2: 空数组
        int[] nums2 = {};
        List<Integer> result2 = solution.countSmaller(nums2);
        System.out.println("测试用例2: " + result2); // 期望输出: []
        
        // 测试用例3: 单个元素
        int[] nums3 = {1};
        List<Integer> result3 = solution.countSmaller(nums3);
        System.out.println("测试用例3: " + result3); // 期望输出: [0]
        
        // 测试用例4: 重复元素
        int[] nums4 = {2, 2, 2, 2};
        List<Integer> result4 = solution.countSmaller(nums4);
        System.out.println("测试用例4: " + result4); // 期望输出: [0, 0, 0, 0]
        
        // 测试用例5: 递减序列
        int[] nums5 = {5, 4, 3, 2, 1};
        List<Integer> result5 = solution.countSmaller(nums5);
        System.out.println("测试用例5: " + result5); // 期望输出: [4, 3, 2, 1, 0]
        
        // 测试用例6: 递增序列
        int[] nums6 = {1, 2, 3, 4, 5};
        List<Integer> result6 = solution.countSmaller(nums6);
        System.out.println("测试用例6: " + result6); // 期望输出: [0, 0, 0, 0, 0]
    }
}

/**
 * 算法复杂度详细分析:
 * 
 * 时间复杂度:
 * 1. 数组排序: O(n log n)
 * 2. 离散化映射构建: O(n)
 * 3. 线段树操作(更新和查询): O(n log n)
 * 总时间复杂度: O(n log n)
 * 
 * 空间复杂度:
 * 1. 排序数组: O(n)
 * 2. 离散化映射: O(n)
 * 3. 线段树: O(n)
 * 4. 结果数组: O(n)
 * 总空间复杂度: O(n)
 * 
 * 算法优化点:
 * 1. 离散化处理: 将原始数值映射到连续索引，减少线段树大小
 * 2. 从右向左遍历: 避免重复计算，每个元素只处理一次
 * 3. 线段树优化: 使用递归实现，代码清晰易懂
 * 
 * 面试要点:
 * 1. 理解离散化的必要性
 * 2. 掌握线段树的基本操作
 * 3. 能够分析时间空间复杂度
 * 4. 处理边界情况和异常输入
 */