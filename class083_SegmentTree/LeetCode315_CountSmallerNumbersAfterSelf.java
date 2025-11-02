// LeetCode 315. Count of Smaller Numbers After Self
// 题目链接: https://leetcode.cn/problems/count-of-smaller-numbers-after-self/
// 题目描述:
// 给你一个整数数组 nums ，按要求返回一个新数组 counts 。数组 counts 有该性质： 
// counts[i] 的值是 nums[i] 右侧小于 nums[i] 的元素的数量。
//
// 示例 1:
// 输入: nums = [5,2,6,1]
// 输出: [2,1,1,0]
// 解释:
// 5 的右侧有 2 个更小的元素 (2 和 1)
// 2 的右侧有 1 个更小的元素 (1)
// 6 的右侧有 1 个更小的元素 (1)
// 1 的右侧有 0 个更小的元素
//
// 示例 2:
// 输入: nums = [-1]
// 输出: [0]
//
// 示例 3:
// 输入: nums = [-1,-1]
// 输出: [0,0]
//
// 提示:
// 1 <= nums.length <= 10^5
// -10^4 <= nums[i] <= 10^4
//
// 解题思路:
// 这是一个经典的逆序对问题，可以使用线段树来解决。
// 1. 由于数值范围较大(-10^4 到 10^4)，需要进行离散化处理
// 2. 从右往左遍历数组，对于每个元素:
//    - 查询线段树中比当前元素小的元素个数
//    - 将当前元素插入线段树
// 3. 使用线段树维护区间和，支持单点更新和区间查询
//
// 时间复杂度: O(n log n)，其中n是数组长度
// 空间复杂度: O(n)

import java.util.*;

public class LeetCode315_CountSmallerNumbersAfterSelf {
    // 线段树节点
    static class Node {
        int l, r;  // 区间左右端点
        int sum;   // 区间和
        
        public Node(int l, int r) {
            this.l = l;
            this.r = r;
        }
    }
    
    // 线段树数组
    private Node[] tree;
    
    // 离散化后的数组
    private int[] nums;
    
    // 离散化映射
    private Map<Integer, Integer> map;
    
    // 离散化数组大小
    private int n;
    
    public List<Integer> countSmaller(int[] nums) {
        int len = nums.length;
        List<Integer> result = new ArrayList<>();
        
        // 特殊情况处理
        if (len == 0) {
            return result;
        }
        
        // 离散化处理
        discretization(nums);
        
        // 初始化线段树
        tree = new Node[n * 4];
        build(1, n, 1);
        
        // 从右往左遍历
        for (int i = len - 1; i >= 0; i--) {
            // 查询比当前元素小的元素个数
            int index = map.get(nums[i]);
            int count = query(1, index - 1, 1, n, 1);
            result.add(count);
            
            // 将当前元素插入线段树
            update(index, 1, n, 1);
        }
        
        // 结果需要反转
        Collections.reverse(result);
        return result;
    }
    
    // 离散化处理
    private void discretization(int[] nums) {
        Set<Integer> set = new HashSet<>();
        for (int num : nums) {
            set.add(num);
        }
        
        // 排序去重后的数值
        this.nums = new int[set.size()];
        int index = 0;
        for (int num : set) {
            this.nums[index++] = num;
        }
        Arrays.sort(this.nums);
        
        // 建立映射关系
        map = new HashMap<>();
        for (int i = 0; i < this.nums.length; i++) {
            map.put(this.nums[i], i + 1);
        }
        
        this.n = this.nums.length;
    }
    
    // 建立线段树
    private void build(int l, int r, int i) {
        tree[i] = new Node(l, r);
        if (l == r) {
            return;
        }
        int mid = (l + r) >> 1;
        build(l, mid, i << 1);
        build(mid + 1, r, i << 1 | 1);
    }
    
    // 单点更新
    private void update(int index, int l, int r, int i) {
        if (l == r) {
            tree[i].sum++;
            return;
        }
        int mid = (l + r) >> 1;
        if (index <= mid) {
            update(index, l, mid, i << 1);
        } else {
            update(index, mid + 1, r, i << 1 | 1);
        }
        pushUp(i);
    }
    
    // 向上传递
    private void pushUp(int i) {
        tree[i].sum = tree[i << 1].sum + tree[i << 1 | 1].sum;
    }
    
    // 区间查询
    private int query(int jobl, int jobr, int l, int r, int i) {
        if (jobl > r || jobr < l) {
            return 0;
        }
        if (jobl <= l && r <= jobr) {
            return tree[i].sum;
        }
        int mid = (l + r) >> 1;
        int ans = 0;
        if (jobl <= mid) {
            ans += query(jobl, jobr, l, mid, i << 1);
        }
        if (jobr > mid) {
            ans += query(jobl, jobr, mid + 1, r, i << 1 | 1);
        }
        return ans;
    }
    
    // 测试函数
    public static void main(String[] args) {
        LeetCode315_CountSmallerNumbersAfterSelf solution = new LeetCode315_CountSmallerNumbersAfterSelf();
        
        // 测试用例1
        int[] nums1 = {5, 2, 6, 1};
        List<Integer> result1 = solution.countSmaller(nums1);
        System.out.println("输入: [5,2,6,1]");
        System.out.println("输出: " + result1);
        System.out.println("期望: [2,1,1,0]");
        System.out.println();
        
        // 测试用例2
        int[] nums2 = {-1};
        List<Integer> result2 = solution.countSmaller(nums2);
        System.out.println("输入: [-1]");
        System.out.println("输出: " + result2);
        System.out.println("期望: [0]");
        System.out.println();
        
        // 测试用例3
        int[] nums3 = {-1, -1};
        List<Integer> result3 = solution.countSmaller(nums3);
        System.out.println("输入: [-1,-1]");
        System.out.println("输出: " + result3);
        System.out.println("期望: [0,0]");
    }
}