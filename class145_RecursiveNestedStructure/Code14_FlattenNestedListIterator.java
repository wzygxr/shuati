package class039;

import java.util.*;

/**
 * LeetCode 341. Flatten Nested List Iterator (扁平化嵌套列表迭代器)
 * 来源: LeetCode
 * 网址: https://leetcode.cn/problems/flatten-nested-list-iterator/
 * 
 * 题目描述:
 * 给你一个嵌套的整数列表 nestedList 。每个元素要么是一个整数，要么是一个列表；该列表的元素也可能是整数或者是其他列表。
 * 请你实现一个迭代器将其扁平化，使之能够遍历这个列表中的所有整数。
 * 实现扁平迭代器接口 NestedIterator ：
 * - NestedIterator(List<NestedInteger> nestedList) 用嵌套列表 nestedList 初始化迭代器。
 * - int next() 返回嵌套列表的下一个整数。
 * - boolean hasNext() 如果仍然存在待迭代的整数，返回 true ；否则，返回 false 。
 * 
 * 示例:
 * 输入: nestedList = [[1,1],2,[1,1]]
 * 输出: [1,1,2,1,1]
 * 解释: 通过重复调用 next() 直到 hasNext() 返回 false，next() 返回的元素的顺序应该是: [1,1,2,1,1]。
 * 
 * 解题思路:
 * 方法1: 预计算所有整数并存储在列表中
 * 方法2: 使用栈进行惰性计算（惰性迭代器）
 * 这里使用方法2，更节省空间，符合迭代器的惰性计算原则
 * 
 * 时间复杂度:
 * - 构造函数: O(k)，其中k是嵌套列表中整数的总数量
 * - next(): O(1)
 * - hasNext(): O(1)，最坏情况下可能为O(k)，但均摊分析仍为O(1)
 * 
 * 空间复杂度: O(d)，其中d是嵌套列表的最大深度
 */

// 这是题目给定的接口，在实际提交时不需要实现
interface NestedInteger {
    boolean isInteger();
    Integer getInteger();
    List<NestedInteger> getList();
}

// 为了测试创建的示例实现类
class NestedIntegerImpl implements NestedInteger {
    private Integer value;
    private List<NestedInteger> list;
    
    // 创建整数类型的NestedInteger
    public NestedIntegerImpl(Integer value) {
        this.value = value;
        this.list = null;
    }
    
    // 创建列表类型的NestedInteger
    public NestedIntegerImpl() {
        this.value = null;
        this.list = new ArrayList<>();
    }
    
    // 向列表中添加元素
    public void add(NestedInteger ni) {
        if (list == null) {
            list = new ArrayList<>();
            value = null;
        }
        list.add(ni);
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
        return list;
    }
}

public class Code14_FlattenNestedListIterator implements Iterator<Integer> {
    // 使用栈存储嵌套列表的迭代器，以便回溯
    private Stack<Iterator<NestedInteger>> stack;
    // 指向下一个要返回的整数
    private Integer nextVal;
    
    public Code14_FlattenNestedListIterator(List<NestedInteger> nestedList) {
        stack = new Stack<>();
        // 将顶层列表的迭代器压入栈中
        if (nestedList != null && !nestedList.isEmpty()) {
            stack.push(nestedList.iterator());
        }
        // 预先寻找第一个整数
        nextVal = findNextInteger();
    }
    
    @Override
    public Integer next() {
        if (!hasNext()) {
            throw new NoSuchElementException("No more integers in the nested list");
        }
        // 保存当前的nextVal
        Integer result = nextVal;
        // 寻找下一个整数
        nextVal = findNextInteger();
        return result;
    }
    
    @Override
    public boolean hasNext() {
        return nextVal != null;
    }
    
    /**
     * 查找下一个整数
     * 使用栈进行深度优先搜索，直到找到一个整数或者栈为空
     */
    private Integer findNextInteger() {
        while (!stack.isEmpty()) {
            Iterator<NestedInteger> currentIterator = stack.peek();
            
            // 如果当前迭代器已经遍历完，则弹出栈顶
            if (!currentIterator.hasNext()) {
                stack.pop();
                continue;
            }
            
            // 获取下一个元素
            NestedInteger nextNested = currentIterator.next();
            
            // 如果是整数，直接返回
            if (nextNested.isInteger()) {
                return nextNested.getInteger();
            } else {
                // 如果是列表，将其迭代器压入栈中
                List<NestedInteger> nestedList = nextNested.getList();
                if (!nestedList.isEmpty()) {
                    stack.push(nestedList.iterator());
                }
            }
        }
        
        // 栈为空，没有更多整数
        return null;
    }
    
    /**
     * 方法2: 预计算所有整数并存储在列表中
     * 这是一个更简单但可能占用更多空间的实现
     */
    private static class PreComputeNestedListIterator implements Iterator<Integer> {
        private List<Integer> flattenedList;
        private int index;
        
        public PreComputeNestedListIterator(List<NestedInteger> nestedList) {
            flattenedList = new ArrayList<>();
            index = 0;
            // 预先递归展平整个嵌套列表
            flatten(nestedList, flattenedList);
        }
        
        private void flatten(List<NestedInteger> nestedList, List<Integer> result) {
            for (NestedInteger ni : nestedList) {
                if (ni.isInteger()) {
                    result.add(ni.getInteger());
                } else {
                    flatten(ni.getList(), result);
                }
            }
        }
        
        @Override
        public boolean hasNext() {
            return index < flattenedList.size();
        }
        
        @Override
        public Integer next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            return flattenedList.get(index++);
        }
    }
    
    // 测试函数
    public static void main(String[] args) {
        // 测试用例1: [[1,1],2,[1,1]]
        List<NestedInteger> testCase1 = new ArrayList<>();
        
        // [1,1]
        NestedIntegerImpl list1 = new NestedIntegerImpl();
        list1.add(new NestedIntegerImpl(1));
        list1.add(new NestedIntegerImpl(1));
        testCase1.add(list1);
        
        // 2
        testCase1.add(new NestedIntegerImpl(2));
        
        // [1,1]
        NestedIntegerImpl list2 = new NestedIntegerImpl();
        list2.add(new NestedIntegerImpl(1));
        list2.add(new NestedIntegerImpl(1));
        testCase1.add(list2);
        
        System.out.println("测试用例1 (惰性迭代器):");
        System.out.println("输入: [[1,1],2,[1,1]]");
        Code14_FlattenNestedListIterator iterator1 = new Code14_FlattenNestedListIterator(testCase1);
        System.out.print("输出: [");
        boolean first = true;
        while (iterator1.hasNext()) {
            if (!first) {
                System.out.print(", ");
            }
            System.out.print(iterator1.next());
            first = false;
        }
        System.out.println("]");
        System.out.println("期望: [1, 1, 2, 1, 1]");
        System.out.println();
        
        // 重置测试用例（因为前面对象已经被消费）
        List<NestedInteger> testCase1Again = new ArrayList<>();
        NestedIntegerImpl list1Again = new NestedIntegerImpl();
        list1Again.add(new NestedIntegerImpl(1));
        list1Again.add(new NestedIntegerImpl(1));
        testCase1Again.add(list1Again);
        testCase1Again.add(new NestedIntegerImpl(2));
        NestedIntegerImpl list2Again = new NestedIntegerImpl();
        list2Again.add(new NestedIntegerImpl(1));
        list2Again.add(new NestedIntegerImpl(1));
        testCase1Again.add(list2Again);
        
        System.out.println("测试用例1 (预计算迭代器):");
        PreComputeNestedListIterator preIterator1 = new PreComputeNestedListIterator(testCase1Again);
        System.out.print("输出: [");
        first = true;
        while (preIterator1.hasNext()) {
            if (!first) {
                System.out.print(", ");
            }
            System.out.print(preIterator1.next());
            first = false;
        }
        System.out.println("]");
        System.out.println("期望: [1, 1, 2, 1, 1]");
        System.out.println();
        
        // 测试用例2: [1,[4,[6]]]
        List<NestedInteger> testCase2 = new ArrayList<>();
        testCase2.add(new NestedIntegerImpl(1));
        
        NestedIntegerImpl outerList = new NestedIntegerImpl();
        outerList.add(new NestedIntegerImpl(4));
        
        NestedIntegerImpl innerList = new NestedIntegerImpl();
        innerList.add(new NestedIntegerImpl(6));
        outerList.add(innerList);
        
        testCase2.add(outerList);
        
        System.out.println("测试用例2 (惰性迭代器):");
        System.out.println("输入: [1,[4,[6]]]");
        Code14_FlattenNestedListIterator iterator2 = new Code14_FlattenNestedListIterator(testCase2);
        System.out.print("输出: [");
        first = true;
        while (iterator2.hasNext()) {
            if (!first) {
                System.out.print(", ");
            }
            System.out.print(iterator2.next());
            first = false;
        }
        System.out.println("]");
        System.out.println("期望: [1, 4, 6]");
    }
}