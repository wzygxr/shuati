package class131;

import java.util.*;

/**
 * SPOJ MKTHNUM. K-th Number (主席树解法)
 * 题目链接: https://www.spoj.com/problems/MKTHNUM/
 * 题目描述: 给定一个数组，查询区间[l,r]内第k小的元素
 *
 * 解题思路:
 * 使用主席树(可持久化线段树)实现区间第k小元素查询
 * 1. 对数组元素进行离散化处理
 * 2. 构建主席树，每个版本表示前缀数组的信息
 * 3. 通过两个版本的差值查询区间信息
 * 
 * 时间复杂度分析:
 * - 构建主席树: O(n log n)
 * - 区间查询: O(log n)
 * 空间复杂度: O(n log n) 主席树需要约n log n的空间
 */
public class Code16_KthNumber {
    
    // 主席树节点定义
    static class ChairmanTreeNode {
        int left, right;
        int count; // 区间内元素个数
        ChairmanTreeNode leftChild, rightChild;
        
        public ChairmanTreeNode(int left, int right) {
            this.left = left;
            this.right = right;
            this.count = 0;
            this.leftChild = null;
            this.rightChild = null;
        }
    }
    
    private int[] nums;
    private int[] sorted;
    private Map<Integer, Integer> ranks;
    private ChairmanTreeNode[] roots;
    private int versionCount;
    
    public Code16_KthNumber(int[] nums) {
        this.nums = nums.clone();
        this.versionCount = nums.length;
        this.roots = new ChairmanTreeNode[versionCount + 1];
        
        // 离散化处理
        this.sorted = nums.clone();
        Arrays.sort(sorted);
        this.ranks = new HashMap<>();
        int rank = 1;
        for (int num : sorted) {
            if (!ranks.containsKey(num)) {
                ranks.put(num, rank++);
            }
        }
        
        // 构建主席树
        build();
    }
    
    // 构建主席树
    private void build() {
        roots[0] = new ChairmanTreeNode(1, ranks.size());
        for (int i = 1; i <= versionCount; i++) {
            roots[i] = update(roots[i - 1], ranks.get(nums[i - 1]));
        }
    }
    
    // 更新主席树，插入一个元素
    private ChairmanTreeNode update(ChairmanTreeNode prev, int value) {
        ChairmanTreeNode root = new ChairmanTreeNode(prev.left, prev.right);
        root.count = prev.count + 1;
        
        if (prev.left == prev.right) {
            return root;
        }
        
        int mid = (prev.left + prev.right) / 2;
        if (value <= mid) {
            root.leftChild = update(prev.leftChild != null ? prev.leftChild : new ChairmanTreeNode(prev.left, mid), value);
            root.rightChild = prev.rightChild;
        } else {
            root.leftChild = prev.leftChild;
            root.rightChild = update(prev.rightChild != null ? prev.rightChild : new ChairmanTreeNode(mid + 1, prev.right), value);
        }
        
        return root;
    }
    
    // 查询区间第k小元素
    public int kthNumber(int left, int right, int k) {
        return query(roots[left - 1], roots[right], k);
    }
    
    // 查询两个版本的差值中的第k小元素
    private int query(ChairmanTreeNode leftRoot, ChairmanTreeNode rightRoot, int k) {
        if (leftRoot.left == leftRoot.right) {
            return sorted[leftRoot.left - 1];
        }
        
        int leftCount = (rightRoot.leftChild != null ? rightRoot.leftChild.count : 0) - 
                        (leftRoot.leftChild != null ? leftRoot.leftChild.count : 0);
        
        if (k <= leftCount) {
            return query(leftRoot.leftChild != null ? leftRoot.leftChild : new ChairmanTreeNode(leftRoot.left, (leftRoot.left + leftRoot.right) / 2),
                         rightRoot.leftChild != null ? rightRoot.leftChild : new ChairmanTreeNode(rightRoot.left, (rightRoot.left + rightRoot.right) / 2),
                         k);
        } else {
            return query(leftRoot.rightChild != null ? leftRoot.rightChild : new ChairmanTreeNode((leftRoot.left + leftRoot.right) / 2 + 1, leftRoot.right),
                         rightRoot.rightChild != null ? rightRoot.rightChild : new ChairmanTreeNode((rightRoot.left + rightRoot.right) / 2 + 1, rightRoot.right),
                         k - leftCount);
        }
    }
    
    // 测试方法
    public static void main(String[] args) {
        // 示例测试
        int[] nums = {1, 5, 2, 6, 3, 7, 4};
        Code16_KthNumber solution = new Code16_KthNumber(nums);
        
        System.out.println("初始数组: " + Arrays.toString(nums));
        System.out.println("区间[2,5]第2小的元素: " + solution.kthNumber(2, 5, 2)); // [5,2,6,3]中第2小的是3
        System.out.println("区间[1,7]第3小的元素: " + solution.kthNumber(1, 7, 3)); // 全部元素中第3小的是3
        System.out.println("区间[4,6]第1小的元素: " + solution.kthNumber(4, 6, 1)); // [6,3,7]中第1小的是3
    }
}