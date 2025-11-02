package class039;

import java.util.*;

/**
 * LeetCode 364. Nested List Weight Sum II (嵌套列表权重和 II)
 * 来源: LeetCode
 * 网址: https://leetcode.cn/problems/nested-list-weight-sum-ii/
 * 
 * 题目描述:
 * 给定一个嵌套的整数列表 nestedList，每个元素要么是整数，要么是列表。同时，列表中元素同样也可以是整数或者是另一个列表。
 * 整数的权重与其深度成反比，深度最大的整数权重为 1，深度第二大的整数权重为 2，依此类推。
 * 返回该列表的加权和。
 * 
 * 示例:
 * 输入: [[1,1],2,[1,1]]
 * 输出: 8
 * 解释: 四个 1 位于深度为 1 的位置，一个 2 位于深度为 2 的位置。
 * 4*1*2 + 1*2*1 = 8 + 2 = 10？不，这里权重与深度成反比。
 * 正确计算: 4*1*1 + 1*2*2 = 4 + 4 = 8
 * 
 * 解题思路:
 * 方法1：先计算最大深度，然后使用深度的倒数作为权重
 * 方法2：使用迭代方法，每遍历一层，累加当前层的和，并将其加入下一层的权重计算
 * 这里使用方法2，更高效且简洁
 * 
 * 时间复杂度: O(n)，其中n是所有整数元素的总数
 * 空间复杂度: O(d)，其中d是嵌套列表的最大深度
 */

public class Code12_NestedListWeightSumII {

    // 计算嵌套列表的反向加权和
    public int depthSumInverse(List<NestedInteger> nestedList) {
        // 使用迭代方法，累加每层的和
        int sum = 0; // 当前所有层的和
        int weightedSum = 0; // 最终的加权和
        
        // 当嵌套列表不为空时，继续处理
        while (!nestedList.isEmpty()) {
            List<NestedInteger> nextLevel = new ArrayList<>();
            int levelSum = 0;
            
            // 处理当前层的所有元素
            for (NestedInteger ni : nestedList) {
                if (ni.isInteger()) {
                    // 如果是整数，加到当前层的和中
                    levelSum += ni.getInteger();
                } else {
                    // 如果是列表，将其元素加入下一层
                    nextLevel.addAll(ni.getList());
                }
            }
            
            // 将当前层的和累加到总和中，这样每增加一层，前面层的和就会被多计算一次
            // 这等价于将权重设置为(最大深度 - 当前深度 + 1)
            sum += levelSum;
            weightedSum += sum;
            
            // 处理下一层
            nestedList = nextLevel;
        }
        
        return weightedSum;
    }

    // 递归方法实现：先计算最大深度，然后使用权重 = 最大深度 - 当前深度 + 1
    public int depthSumInverseRecursive(List<NestedInteger> nestedList) {
        // 步骤1: 计算最大深度
        int maxDepth = getMaxDepth(nestedList);
        
        // 步骤2: 使用递归计算加权和，权重 = maxDepth - depth + 1
        return dfs(nestedList, 1, maxDepth);
    }
    
    // 计算嵌套列表的最大深度
    private int getMaxDepth(List<NestedInteger> nestedList) {
        if (nestedList.isEmpty()) {
            return 0;
        }
        
        int maxDepth = 0;
        for (NestedInteger ni : nestedList) {
            if (ni.isInteger()) {
                maxDepth = Math.max(maxDepth, 1);
            } else {
                maxDepth = Math.max(maxDepth, 1 + getMaxDepth(ni.getList()));
            }
        }
        
        return maxDepth;
    }
    
    // 递归深度优先搜索函数
    private int dfs(List<NestedInteger> nestedList, int currentDepth, int maxDepth) {
        int sum = 0;
        int weight = maxDepth - currentDepth + 1;
        
        for (NestedInteger ni : nestedList) {
            if (ni.isInteger()) {
                sum += ni.getInteger() * weight;
            } else {
                sum += dfs(ni.getList(), currentDepth + 1, maxDepth);
            }
        }
        
        return sum;
    }

    /**
     * 测试用例说明：
     * 使用前面定义的NestedIntegerImpl来进行测试
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
        Code12_NestedListWeightSumII solution = new Code12_NestedListWeightSumII();

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

        System.out.println("测试用例1 (迭代方法):");
        System.out.println("输入: [[1,1],2,[1,1]]");
        System.out.println("输出: " + solution.depthSumInverse(testCase1));
        System.out.println("期望: 8");
        System.out.println();

        System.out.println("测试用例1 (递归方法):");
        System.out.println("输入: [[1,1],2,[1,1]]");
        System.out.println("输出: " + solution.depthSumInverseRecursive(testCase1));
        System.out.println("期望: 8");
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

        System.out.println("测试用例2 (迭代方法):");
        System.out.println("输入: [1,[4,[6]]]");
        System.out.println("输出: " + solution.depthSumInverse(testCase2));
        System.out.println("期望: 17");
        System.out.println();

        System.out.println("测试用例2 (递归方法):");
        System.out.println("输入: [1,[4,[6]]]");
        System.out.println("输出: " + solution.depthSumInverseRecursive(testCase2));
        System.out.println("期望: 17");
    }
}