package class036;

import java.util.*;

/**
 * AcWing 44. 二叉搜索树的后序遍历序列
 * 题目链接: https://www.acwing.com/problem/content/44/
 * 题目描述: 输入一个整数数组，判断该数组是不是某二叉搜索树的后序遍历结果。
 * 
 * 核心算法思想:
 * 1. 递归分治: 利用二叉搜索树的性质（左子树 < 根 < 右子树）
 * 2. 单调栈: 利用后序遍历的单调性特点
 * 3. 迭代优化: 使用栈模拟递归过程
 * 
 * 时间复杂度分析:
 * - 方法1(递归): O(N²) - 最坏情况斜树
 * - 方法2(单调栈): O(N) - 每个元素入栈出栈一次
 * - 方法3(优化递归): O(NlogN) - 平衡树情况
 * 
 * 空间复杂度分析:
 * - 方法1(递归): O(N) - 递归调用栈深度
 * - 方法2(单调栈): O(N) - 栈空间
 * - 方法3(优化递归): O(logN) - 平衡树递归深度
 * 
 * 相关题目:
 * 1. LeetCode 255. 验证前序遍历序列二叉搜索树 - 前序遍历版本
 * 2. LeetCode 105. 从前序与中序遍历序列构造二叉树 - 构造树
 * 3. 剑指 Offer 33. 二叉搜索树的后序遍历序列 - 相同题目
 * 
 * 工程化考量:
 * 1. 边界处理: 空数组、单元素数组
 * 2. 数值范围: 处理重复元素的情况
 * 3. 性能优化: 选择合适算法应对不同数据规模
 */
public class AcWing44_二叉搜索树的后序遍历序列 {
    
    /**
     * 方法1: 递归分治法 - 基础实现
     * 思路: 利用二叉搜索树的性质进行递归验证
     * 时间复杂度: O(N²) - 最坏情况斜树
     * 空间复杂度: O(N) - 递归调用栈深度
     */
    public static boolean verifySequenceOfBST1(int[] sequence) {
        if (sequence == null || sequence.length == 0) {
            return true;
        }
        return verify(sequence, 0, sequence.length - 1);
    }
    
    private static boolean verify(int[] sequence, int start, int end) {
        if (start >= end) {
            return true;
        }
        
        // 根节点是最后一个元素
        int root = sequence[end];
        int i = start;
        
        // 找到左子树边界（所有小于根节点的元素）
        while (i < end && sequence[i] < root) {
            i++;
        }
        
        int mid = i; // 左子树结束位置
        
        // 检查右子树是否都大于根节点
        while (i < end) {
            if (sequence[i] <= root) {
                return false;
            }
            i++;
        }
        
        // 递归验证左右子树
        return verify(sequence, start, mid - 1) && 
               verify(sequence, mid, end - 1);
    }
    
    /**
     * 方法2: 单调栈法 - 最优解法
     * 思路: 利用后序遍历的单调性特点，使用栈辅助验证
     * 时间复杂度: O(N) - 每个元素入栈出栈一次
     * 空间复杂度: O(N) - 栈空间
     * 
     * 核心思想:
     * 1. 后序遍历的逆序是"根-右-左"
     * 2. 维护一个单调递增的栈
     * 3. 记录当前子树的根节点作为最小值边界
     */
    public static boolean verifySequenceOfBST2(int[] sequence) {
        if (sequence == null || sequence.length == 0) {
            return true;
        }
        
        Stack<Integer> stack = new Stack<>();
        int root = Integer.MAX_VALUE; // 初始化根节点为最大值
        
        // 逆序遍历（根-右-左）
        for (int i = sequence.length - 1; i >= 0; i--) {
            // 如果当前节点大于根节点，违反二叉搜索树性质
            if (sequence[i] > root) {
                return false;
            }
            
            // 维护单调栈（找到当前节点的根节点）
            while (!stack.isEmpty() && sequence[i] < stack.peek()) {
                root = stack.pop();
            }
            
            stack.push(sequence[i]);
        }
        
        return true;
    }
    
    /**
     * 方法3: 优化递归法 - 避免最坏情况
     * 思路: 在递归过程中进行优化，减少不必要的比较
     * 时间复杂度: O(NlogN) - 平衡树情况
     * 空间复杂度: O(logN) - 平衡树递归深度
     */
    public static boolean verifySequenceOfBST3(int[] sequence) {
        if (sequence == null || sequence.length == 0) {
            return true;
        }
        return verifyOptimized(sequence, 0, sequence.length - 1);
    }
    
    private static boolean verifyOptimized(int[] sequence, int start, int end) {
        if (start >= end) {
            return true;
        }
        
        int root = sequence[end];
        int leftEnd = start - 1;
        int rightStart = end;
        
        // 同时从两端向中间扫描，找到左右子树分界
        int i = start, j = end - 1;
        while (i <= j) {
            if (sequence[i] < root) {
                leftEnd = i;
                i++;
            } else if (sequence[j] > root) {
                rightStart = j;
                j--;
            } else {
                // 找到违反规则的元素
                return false;
            }
        }
        
        // 递归验证左右子树
        return verifyOptimized(sequence, start, leftEnd) && 
               verifyOptimized(sequence, rightStart, end - 1);
    }
    
    /**
     * 测试方法: 包含多种测试用例
     */
    public static void main(String[] args) {
        System.out.println("========== AcWing 44 测试 ==========");
        
        // 测试用例1: 有效的后序遍历 [1,3,2,5,7,6,4]
        int[] seq1 = {1, 3, 2, 5, 7, 6, 4};
        System.out.println("测试用例1: " + Arrays.toString(seq1));
        System.out.println("方法1结果: " + verifySequenceOfBST1(seq1));
        System.out.println("方法2结果: " + verifySequenceOfBST2(seq1));
        System.out.println("方法3结果: " + verifySequenceOfBST3(seq1));
        
        // 测试用例2: 无效的后序遍历 [1,6,3,2,5]
        int[] seq2 = {1, 6, 3, 2, 5};
        System.out.println("\n测试用例2: " + Arrays.toString(seq2));
        System.out.println("方法1结果: " + verifySequenceOfBST1(seq2));
        System.out.println("方法2结果: " + verifySequenceOfBST2(seq2));
        System.out.println("方法3结果: " + verifySequenceOfBST3(seq2));
        
        // 测试用例3: 单元素数组
        int[] seq3 = {5};
        System.out.println("\n测试用例3: " + Arrays.toString(seq3));
        System.out.println("方法1结果: " + verifySequenceOfBST1(seq3));
        
        // 测试用例4: 空数组
        int[] seq4 = {};
        System.out.println("\n测试用例4: " + Arrays.toString(seq4));
        System.out.println("方法1结果: " + verifySequenceOfBST1(seq4));
        
        // 性能对比说明
        System.out.println("\n========== 性能对比说明 ==========");
        System.out.println("1. 方法1（递归分治）: 逻辑清晰，但最坏情况O(N²)");
        System.out.println("2. 方法2（单调栈）: 最优解法O(N)，推荐使用");
        System.out.println("3. 方法3（优化递归）: 平衡树情况O(NlogN)");
    }
}

/*
Python实现:

class Solution:
    # 方法2: 单调栈法（最优解）
    def verifySequenceOfBST(self, sequence: List[int]) -> bool:
        if not sequence:
            return True
            
        stack = []
        root = float('inf')
        
        # 逆序遍历
        for i in range(len(sequence)-1, -1, -1):
            if sequence[i] > root:
                return False
                
            while stack and sequence[i] < stack[-1]:
                root = stack.pop()
                
            stack.append(sequence[i])
            
        return True

C++实现:

#include <iostream>
#include <vector>
#include <stack>
#include <climits>
using namespace std;

class Solution {
public:
    // 方法2: 单调栈法（最优解）
    bool verifySequenceOfBST(vector<int> sequence) {
        if (sequence.empty()) return true;
        
        stack<int> stk;
        int root = INT_MAX;
        
        for (int i = sequence.size() - 1; i >= 0; i--) {
            if (sequence[i] > root) return false;
            
            while (!stk.empty() && sequence[i] < stk.top()) {
                root = stk.top();
                stk.pop();
            }
            
            stk.push(sequence[i]);
        }
        
        return true;
    }
};
*/