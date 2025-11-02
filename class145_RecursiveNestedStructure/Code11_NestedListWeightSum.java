package class039;

import java.util.*;

/**
 * LeetCode 339. Nested List Weight Sum (嵌套列表权重和)
 * 来源: LeetCode
 * 网址: https://leetcode.cn/problems/nested-list-weight-sum/
 * 
 * 题目描述:
 * 给定一个嵌套的整数列表 nestedList，每个元素要么是整数，要么是列表。同时，列表中元素同样也可以是整数或者是另一个列表。
 * 整数的权重是其深度，返回该列表的加权和。
 * 
 * 示例:
 * 输入: [[1,1],2,[1,1]]
 * 输出: 10
 * 解释: 因为这个列表中有四个深度为 2 的 1 ，和一个深度为 1 的 2。
 * 4*1*2 + 1*2*1 = 8 + 2 = 10
 * 
 * 解题思路:
 * 使用递归处理嵌套结构，对每个元素进行深度遍历，累加每个整数与其深度的乘积。
 * 
 * 时间复杂度: O(n)，其中n是所有整数元素的总数
 * 空间复杂度: O(d)，其中d是嵌套列表的最大深度，递归调用栈的深度
 */

// 这是题目中定义的接口，实际提交时不需要自己定义
interface NestedInteger {
    // @return true if this NestedInteger holds a single integer, rather than a nested list.
    boolean isInteger();

    // @return the single integer that this NestedInteger holds, if it holds a single integer
    // Return null if this NestedInteger holds a nested list
    Integer getInteger();

    // @return the nested list that this NestedInteger holds, if it holds a nested list
    // Return empty list if this NestedInteger holds a single integer
    List<NestedInteger> getList();
}

public class Code11_NestedListWeightSum {

    // 计算嵌套列表的加权和
    public int depthSum(List<NestedInteger> nestedList) {
        // 从深度1开始递归计算
        return dfs(nestedList, 1);
    }

    // 递归深度优先搜索函数
    // nestedList: 当前嵌套列表
    // depth: 当前深度
    // return: 当前嵌套列表的加权和
    private int dfs(List<NestedInteger> nestedList, int depth) {
        int sum = 0;
        // 遍历当前列表中的每个元素
        for (NestedInteger ni : nestedList) {
            if (ni.isInteger()) {
                // 如果是整数，累加其值乘以深度
                sum += ni.getInteger() * depth;
            } else {
                // 如果是列表，递归计算其加权和，深度加1
                sum += dfs(ni.getList(), depth + 1);
            }
        }
        return sum;
    }

    /**
     * 测试用例说明：
     * 由于NestedInteger是接口，这里需要创建一个实现类来进行测试
     * 在实际提交时，LeetCode会提供该接口的实现
     */
    // 测试用的NestedInteger实现类
    static class NestedIntegerImpl implements NestedInteger {
        private Integer value;
        private List<NestedInteger> list;

        // 构造整数
        public NestedIntegerImpl(int value) {
            this.value = value;
            this.list = null;
        }

        // 构造空列表
        public NestedIntegerImpl() {
            this.value = null;
            this.list = new ArrayList<>();
        }

        // 向列表中添加元素
        public void add(NestedInteger ni) {
            if (this.list == null) {
                this.list = new ArrayList<>();
                this.value = null;
            }
            this.list.add(ni);
        }

        @Override
        public boolean isInteger() {
            return value != null;
        }

        @Override
        public Integer getInteger() {
            return value;
        }

        @Override
        public List<NestedInteger> getList() {
            return list != null ? list : new ArrayList<>();
        }
    }

    // 主函数，用于测试
    public static void main(String[] args) {
        Code11_NestedListWeightSum solution = new Code11_NestedListWeightSum();

        // 测试用例1: [[1,1],2,[1,1]]
        List<NestedInteger> testCase1 = new ArrayList<>();
        // [1,1]
        NestedInteger list1 = new NestedIntegerImpl();
        list1.add(new NestedIntegerImpl(1));
        list1.add(new NestedIntegerImpl(1));
        testCase1.add(list1);
        // 2
        testCase1.add(new NestedIntegerImpl(2));
        // [1,1]
        NestedInteger list2 = new NestedIntegerImpl();
        list2.add(new NestedIntegerImpl(1));
        list2.add(new NestedIntegerImpl(1));
        testCase1.add(list2);

        System.out.println("测试用例1:");
        System.out.println("输入: [[1,1],2,[1,1]]");
        System.out.println("输出: " + solution.depthSum(testCase1));
        System.out.println("期望: 10");
        System.out.println();

        // 测试用例2: [1,[4,[6]]]
        List<NestedInteger> testCase2 = new ArrayList<>();
        // 1
        testCase2.add(new NestedIntegerImpl(1));
        // [4,[6]]
        NestedInteger outerList = new NestedIntegerImpl();
        outerList.add(new NestedIntegerImpl(4));
        // [6]
        NestedInteger innerList = new NestedIntegerImpl();
        innerList.add(new NestedIntegerImpl(6));
        outerList.add(innerList);
        testCase2.add(outerList);

        System.out.println("测试用例2:");
        System.out.println("输入: [1,[4,[6]]]");
        System.out.println("输出: " + solution.depthSum(testCase2));
        System.out.println("期望: 27");
        System.out.println();

        // 测试用例3: []
        List<NestedInteger> testCase3 = new ArrayList<>();
        System.out.println("测试用例3:");
        System.out.println("输入: []");
        System.out.println("输出: " + solution.depthSum(testCase3));
        System.out.println("期望: 0");
    }
}