package class131;

import java.util.*;

/** 
 * LeetCode 715. Range Module (区间模块)
 * 题目链接: https://leetcode.cn/problems/range-module/
 * 
 * 题目描述: 
 * RangeModule 是一个模块，用于跟踪半开区间 [left, right)。
 * 实现以下方法:
 * 1. RangeModule() 初始化数据结构的对象。
 * 2. void addRange(int left, int right) 添加半开区间 [left, right)。
 * 3. boolean queryRange(int left, int right) 只有在当前正在跟踪区间 [left, right) 时才返回 true。
 * 4. void removeRange(int left, int right) 停止跟踪半开区间 [left, right)。
 * 
 * 解题思路:
 * 使用线段树实现，支持区间添加、查询和删除操作
 * 1. 使用线段树维护区间状态
 * 2. 通过懒惰传播优化区间更新
 * 3. 支持动态开点以节省空间
 * 
 * 时间复杂度分析:
 * - addRange: O(log n)
 * - queryRange: O(log n)
 * - removeRange: O(log n)
 * 空间复杂度: O(q log MAX) 其中q是操作次数，MAX是最大值
 */
public class Code12_RangeModule {
    
    /** 
     * 线段树节点
     * 每个节点表示一个区间[left, right]，并维护该区间的跟踪状态
     */
    static class Node {
        int left, right;              // 节点表示的区间范围
        boolean tracked;              // 区间是否被完全跟踪
        Boolean lazy;                 // 懒惰标记: true表示添加，false表示删除，null表示无操作
        Node leftChild, rightChild;   // 左右子节点
        
        /** 
         * 构造函数
         * 
         * @param l 区间左边界
         * @param r 区间右边界
         */
        Node(int l, int r) {
            left = l;
            right = r;
            tracked = false;
            lazy = null;
        }
    }
    
    private static final int MAX = 1000000000;  // 最大值范围
    private Node root;                          // 线段树根节点
    
    /** 
     * 构造函数，初始化线段树
     */
    public Code12_RangeModule() {
        root = new Node(0, MAX);
    }
    
    /** 
     * 添加半开区间 [left, right)
     * 
     * @param left  区间左边界（包含）
     * @param right 区间右边界（不包含）
     */
    public void addRange(int left, int right) {
        // 注意：内部使用闭区间 [left, right-1]
        update(root, left, right - 1, true);
    }
    
    /** 
     * 查询半开区间 [left, right) 是否被完全跟踪
     * 
     * @param left  区间左边界（包含）
     * @param right 区间右边界（不包含）
     * @return      是否被完全跟踪
     */
    public boolean queryRange(int left, int right) {
        // 注意：内部使用闭区间 [left, right-1]
        return query(root, left, right - 1);
    }
    
    /** 
     * 删除半开区间 [left, right)
     * 
     * @param left  区间左边界（包含）
     * @param right 区间右边界（不包含）
     */
    public void removeRange(int left, int right) {
        // 注意：内部使用闭区间 [left, right-1]
        update(root, left, right - 1, false);
    }
    
    /** 
     * 动态创建子节点
     * 为了节省空间，只在需要时创建子节点
     * 
     * @param node 当前节点
     */
    private void createChildren(Node node) {
        // 只有当子节点不存在时才创建
        if (node.leftChild == null) {
            int mid = node.left + (node.right - node.left) / 2;
            node.leftChild = new Node(node.left, mid);
            node.rightChild = new Node(mid + 1, node.right);
        }
    }
    
    /** 
     * 下传懒惰标记
     * 将当前节点的更新信息传递给子节点
     * 
     * @param node 当前节点
     */
    private void pushDown(Node node) {
        // 只有当节点有懒惰标记时才需要下传
        if (node.lazy != null) {
            // 创建子节点
            createChildren(node);
            
            // 更新子节点的值和懒惰标记
            node.leftChild.tracked = node.lazy;
            node.rightChild.tracked = node.lazy;
            
            node.leftChild.lazy = node.lazy;
            node.rightChild.lazy = node.lazy;
            
            // 清除当前节点的懒惰标记
            node.lazy = null;
        }
    }
    
    /** 
     * 更新区间值
     * 将区间[start, end]内的所有位置的跟踪状态更新为tracked
     * 
     * @param node    当前节点
     * @param start   更新区间起始位置
     * @param end     更新区间结束位置
     * @param tracked 要更新的跟踪状态
     */
    private void update(Node node, int start, int end, boolean tracked) {
        // 当前节点区间与更新区间无交集
        if (start > node.right || end < node.left) {
            return;
        }
        
        // 当前节点区间完全包含在更新区间内
        if (start <= node.left && node.right <= end) {
            node.tracked = tracked;
            node.lazy = tracked;
            return;
        }
        
        // 部分重叠，需要创建子节点
        createChildren(node);
        
        // 下传懒惰标记
        pushDown(node);
        
        // 递归更新子节点
        update(node.leftChild, start, end, tracked);
        update(node.rightChild, start, end, tracked);
        
        // 更新当前节点的跟踪状态为左右子节点跟踪状态的逻辑与
        // 只有当左右子节点都被完全跟踪时，当前节点才被完全跟踪
        node.tracked = node.leftChild.tracked && node.rightChild.tracked;
    }
    
    /** 
     * 查询区间是否被完全跟踪
     * 
     * @param node  当前节点
     * @param start 查询区间起始位置
     * @param end   查询区间结束位置
     * @return      区间是否被完全跟踪
     */
    private boolean query(Node node, int start, int end) {
        // 当前节点区间与查询区间无交集，返回true（不影响结果）
        if (start > node.right || end < node.left) {
            return true;
        }
        
        // 当前节点区间完全包含在查询区间内，或者节点有懒惰标记
        if ((start <= node.left && node.right <= end) || node.lazy != null) {
            return node.tracked;
        }
        
        // 节点没有子节点，返回当前节点的跟踪状态
        if (node.leftChild == null) {
            return node.tracked;
        }
        
        // 递归查询子节点，只有当所有相关子区间都被跟踪时才返回true
        return query(node.leftChild, start, end) && query(node.rightChild, start, end);
    }
    
    /** 
     * 测试方法
     * 
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        Code12_RangeModule rangeModule = new Code12_RangeModule();
        
        rangeModule.addRange(10, 20);
        rangeModule.removeRange(14, 16);
        
        System.out.println(rangeModule.queryRange(10, 14)); // 应该输出true
        System.out.println(rangeModule.queryRange(13, 16)); // 应该输出false
        System.out.println(rangeModule.queryRange(16, 17)); // 应该输出true
    }
}