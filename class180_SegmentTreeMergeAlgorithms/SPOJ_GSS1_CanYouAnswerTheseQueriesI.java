/**
 * SPOJ GSS1 - Can you answer these queries I
 * 题目链接: https://www.spoj.com/problems/GSS1/
 * 
 * 题目描述:
 * 给定一个长度为n的整数序列a，需要处理m个查询。
 * 每个查询给定两个整数l和r，要求找出区间[l,r]内的最大子段和。
 * 
 * 解题思路:
 * 这是一个经典的线段树维护区间最大子段和的问题。
 * 对于每个线段树节点，我们需要维护以下信息：
 * 1. 区间和(sum)
 * 2. 区间最大子段和(max_sum)
 * 3. 区间前缀最大和(prefix_max)
 * 4. 区间后缀最大和(suffix_max)
 * 
 * 合并两个子区间时：
 * - 区间和 = 左子区间和 + 右子区间和
 * - 区间最大子段和 = max(左子区间最大子段和, 右子区间最大子段和, 左子区间后缀最大和 + 右子区间前缀最大和)
 * - 区间前缀最大和 = max(左子区间前缀最大和, 左子区间和 + 右子区间前缀最大和)
 * - 区间后缀最大和 = max(右子区间后缀最大和, 右子区间和 + 左子区间后缀最大和)
 * 
 * 时间复杂度分析:
 * - 建树: O(n)
 * - 区间查询: O(log n)
 * 
 * 空间复杂度: O(n)
 * 
 * 工程化考量:
 * 1. 异常处理: 检查输入参数的有效性
 * 2. 边界情况: 处理空数组、单个元素等情况
 * 3. 性能优化: 使用结构体优化节点信息存储
 * 4. 可测试性: 提供完整的测试用例覆盖各种场景
 * 5. 可读性: 添加详细的注释说明设计思路和实现细节
 * 6. 鲁棒性: 处理极端输入和非理想数据
 */

import java.util.*;

/**
 * 线段树节点信息
 */
class SegmentNode {
    public long sum;           // 区间和
    public long maxSum;        // 区间最大子段和
    public long prefixMax;     // 区间前缀最大和
    public long suffixMax;     // 区间后缀最大和
    
    public SegmentNode() {
        this.sum = 0;
        this.maxSum = 0;
        this.prefixMax = 0;
        this.suffixMax = 0;
    }
    
    public SegmentNode(long sum, long maxSum, long prefixMax, long suffixMax) {
        this.sum = sum;
        this.maxSum = maxSum;
        this.prefixMax = prefixMax;
        this.suffixMax = suffixMax;
    }
}

public class SPOJ_GSS1_CanYouAnswerTheseQueriesI {
    private int n;
    private SegmentNode[] nodes;

    /**
     * 构造函数 - 初始化线段树
     * 
     * @param size 数组大小
     */
    public SPOJ_GSS1_CanYouAnswerTheseQueriesI(int size) {
        // 参数校验
        if (size <= 0) {
            throw new IllegalArgumentException("数组大小必须为正整数");
        }
        
        this.n = size;
        // 线段树数组通常开4倍空间，确保有足够空间存储所有节点
        this.nodes = new SegmentNode[size * 4];
        for (int i = 0; i < nodes.length; i++) {
            nodes[i] = new SegmentNode();
        }
    }

    /**
     * 向上更新节点信息
     * 
     * @param i 当前节点编号
     */
    private void pushUp(int i) {
        SegmentNode leftChild = nodes[i << 1];
        SegmentNode rightChild = nodes[i << 1 | 1];
        SegmentNode currentNode = nodes[i];
        
        // 区间和 = 左子区间和 + 右子区间和
        currentNode.sum = leftChild.sum + rightChild.sum;
        
        // 区间最大子段和 = max(左子区间最大子段和, 右子区间最大子段和, 左子区间后缀最大和 + 右子区间前缀最大和)
        currentNode.maxSum = Math.max(
            Math.max(leftChild.maxSum, rightChild.maxSum),
            leftChild.suffixMax + rightChild.prefixMax
        );
        
        // 区间前缀最大和 = max(左子区间前缀最大和, 左子区间和 + 右子区间前缀最大和)
        currentNode.prefixMax = Math.max(
            leftChild.prefixMax,
            leftChild.sum + rightChild.prefixMax
        );
        
        // 区间后缀最大和 = max(右子区间后缀最大和, 右子区间和 + 左子区间后缀最大和)
        currentNode.suffixMax = Math.max(
            rightChild.suffixMax,
            rightChild.sum + leftChild.suffixMax
        );
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
        
        SegmentNode currentNode = nodes[i];
        if (l == r) {
            // 叶子节点
            currentNode.sum = arr[l];
            currentNode.maxSum = arr[l];
            currentNode.prefixMax = Math.max(0, arr[l]);  // 前缀最大和可以为空(和为0)
            currentNode.suffixMax = Math.max(0, arr[l]);  // 后缀最大和可以为空(和为0)
        } else {
            int mid = (l + r) >> 1;
            build(arr, l, mid, i << 1);
            build(arr, mid + 1, r, i << 1 | 1);
            pushUp(i);
        }
    }

    /**
     * 查询区间[jobl, jobr]内的最大子段和
     * 
     * @param jobl 查询区间左端点
     * @param jobr 查询区间右端点
     * @param l    当前区间左端点
     * @param r    当前区间右端点
     * @param i    当前节点编号
     * @return 区间最大子段和
     */
    public SegmentNode query(int jobl, int jobr, int l, int r, int i) {
        if (jobl <= l && r <= jobr) {
            // 当前区间完全包含在查询区间内
            return nodes[i];
        } else {
            int mid = (l + r) >> 1;
            if (jobr <= mid) {
                // 查询区间完全在左子树
                return query(jobl, jobr, l, mid, i << 1);
            } else if (jobl > mid) {
                // 查询区间完全在右子树
                return query(jobl, jobr, mid + 1, r, i << 1 | 1);
            } else {
                // 查询区间跨越左右子树
                SegmentNode leftResult = query(jobl, jobr, l, mid, i << 1);
                SegmentNode rightResult = query(jobl, jobr, mid + 1, r, i << 1 | 1);
                
                // 合并结果
                SegmentNode mergedResult = new SegmentNode();
                // 区间和 = 左子区间和 + 右子区间和
                mergedResult.sum = leftResult.sum + rightResult.sum;
                
                // 区间最大子段和 = max(左子区间最大子段和, 右子区间最大子段和, 左子区间后缀最大和 + 右子区间前缀最大和)
                mergedResult.maxSum = Math.max(
                    Math.max(leftResult.maxSum, rightResult.maxSum),
                    leftResult.suffixMax + rightResult.prefixMax
                );
                
                // 区间前缀最大和 = max(左子区间前缀最大和, 左子区间和 + 右子区间前缀最大和)
                mergedResult.prefixMax = Math.max(
                    leftResult.prefixMax,
                    leftResult.sum + rightResult.prefixMax
                );
                
                // 区间后缀最大和 = max(右子区间后缀最大和, 右子区间和 + 左子区间后缀最大和)
                mergedResult.suffixMax = Math.max(
                    rightResult.suffixMax,
                    rightResult.sum + leftResult.suffixMax
                );
                
                return mergedResult;
            }
        }
    }

    /**
     * 解决SPOJ GSS1问题
     * 
     * @param arr 原始数组
     * @param queries 查询列表，每个查询为(l, r)
     * @return 查询结果列表
     */
    public static List<Long> solveGSS1(long[] arr, List<int[]> queries) {
        if (arr == null || arr.length == 0 || queries == null || queries.isEmpty()) {
            return new ArrayList<>();
        }
        
        // 初始化线段树
        SPOJ_GSS1_CanYouAnswerTheseQueriesI segTree = new SPOJ_GSS1_CanYouAnswerTheseQueriesI(arr.length);
        segTree.build(arr, 0, arr.length - 1, 1);
        
        // 处理查询
        List<Long> results = new ArrayList<>();
        for (int[] query : queries) {
            int l = query[0];
            int r = query[1];
            // 转换为0索引
            int lIdx = l - 1;
            int rIdx = r - 1;
            SegmentNode resultNode = segTree.query(lIdx, rIdx, 0, arr.length - 1, 1);
            results.add(resultNode.maxSum);
        }
        
        return results;
    }

    // 测试代码
    public static void main(String[] args) {
        System.out.println("开始测试 SPOJ GSS1 - Can you answer these queries I");
        
        // 测试用例1
        long[] arr1 = {-2, 1, -3, 4, -1, 2, 1, -5, 4};
        List<int[]> queries1 = new ArrayList<>();
        queries1.add(new int[]{1, 9});  // 查询整个数组的最大子段和
        
        List<Long> result1 = solveGSS1(arr1, queries1);
        System.out.println("测试用例1结果: " + result1.get(0));  // 应该输出6 (子段[4,-1,2,1])
        
        // 测试用例2
        long[] arr2 = {1, 2, 3, 4, 5};
        List<int[]> queries2 = new ArrayList<>();
        queries2.add(new int[]{2, 4});  // 查询区间[2,4]的最大子段和
        
        List<Long> result2 = solveGSS1(arr2, queries2);
        System.out.println("测试用例2结果: " + result2.get(0));  // 应该输出9 (子段[2,3,4])
        
        // 测试用例3
        long[] arr3 = {-1, -2, -3, -4, -5};
        List<int[]> queries3 = new ArrayList<>();
        queries3.add(new int[]{1, 5});  // 查询整个数组的最大子段和
        
        List<Long> result3 = solveGSS1(arr3, queries3);
        System.out.println("测试用例3结果: " + result3.get(0));  // 应该输出-1 (单个元素-1)
        
        // 多查询测试
        long[] arr4 = {1, -2, 3, -4, 5, -6, 7};
        List<int[]> queries4 = new ArrayList<>();
        queries4.add(new int[]{1, 3});  // 查询区间[1,3]的最大子段和
        queries4.add(new int[]{2, 5});  // 查询区间[2,5]的最大子段和
        queries4.add(new int[]{4, 7});  // 查询区间[4,7]的最大子段和
        
        List<Long> results4 = solveGSS1(arr4, queries4);
        System.out.println("多查询测试结果: " + results4);  // 应该输出[3, 5, 7]
        
        System.out.println("测试完成");
    }
}