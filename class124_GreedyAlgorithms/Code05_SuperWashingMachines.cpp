// 超级洗衣机
// 假设有n台超级洗衣机放在同一排上
// 开始的时候，每台洗衣机内可能有一定量的衣服，也可能是空的
// 在每一步操作中，你可以选择任意 m (1 <= m <= n) 台洗衣机
// 与此同时将每台洗衣机的一件衣服送到相邻的一台洗衣机
// 给定一个整数数组machines代表从左至右每台洗衣机中的衣物数量
// 请给出能让所有洗衣机中剩下的衣物的数量相等的最少的操作步数
// 如果不能使每台洗衣机中衣物的数量相等则返回-1
// 测试链接 : https://leetcode.cn/problems/super-washing-machines/

#define MAXN 100001

/**
 * 超级洗衣机 - 使用贪心算法解决
 * 
 * 算法思路：
 * 这是一个很有趣的贪心问题。关键在于理解每台洗衣机在达到平衡状态前，
 * 需要向左或向右输送多少件衣服。
 * 
 * 解题策略：
 * 1. 首先检查是否能够平均分配衣服，即总衣服数能否被洗衣机台数整除
 * 2. 计算每台洗衣机最终应该拥有的衣服数量（平均值）
 * 3. 对于每台洗衣机，计算它需要向左和向右输送的衣服数量
 * 4. 在每一步中，瓶颈是需要输送衣服数量的最大值
 * 
 * 关键观察：
 * - 每台洗衣机可以同时向左右两个方向输送衣服
 * - 对于位置i，左侧需要的衣服数量为 leftNeed = i * avg - leftSum
 * - 对于位置i，右侧需要的衣服数量为 rightNeed = (n - i - 1) * avg - rightSum
 * - 如果左右两侧都需要衣服，则当前洗衣机是瓶颈，需要的步数是 leftNeed + rightNeed
 * - 否则，瓶颈是 max(|leftNeed|, |rightNeed|)
 * 
 * 时间复杂度：O(n) - 只需遍历数组一次
 * 空间复杂度：O(1) - 只使用了常数额外空间
 * 
 * 是否最优解：是。这是该问题的最优解法。
 * 
 * 适用场景：
 * 1. 资源平衡分配问题
 * 2. 流量控制问题
 * 
 * 相关题目：
 * 1. LeetCode 453. 最小操作次数使数组元素相等 - 数组平衡问题
 * 2. LeetCode 979. 在二叉树中分配硬币 - 树上资源分配
 * 3. LeetCode 1024. 视频拼接 - 区间拼接问题
 * 4. LeetCode 1326. 灌溉花园的最少水龙头数目 - 区间覆盖问题
 * 5. 牛客网 NC135 买票需要多少时间 - 队列模拟相关
 * 6. LintCode 391. 数飞机 - 区间调度相关
 * 7. HackerRank - Jim and the Orders - 贪心调度问题
 * 8. CodeChef - TACHSTCK - 区间配对问题
 * 9. AtCoder ABC104C - All Green - 动态规划相关
 * 10. Codeforces 1363C - Game On Leaves - 博弈论相关
 * 11. SPOJ ANARC08E - Relax! I am a legend - 数学相关
 * 12. POJ 3169 - Layout - 差分约束系统
 * 13. HDU 2586 - How far away? - LCA最近公共祖先
 * 14. USACO 2014 January Silver - Cross Country Skiing - BFS搜索
 * 15. 洛谷 P1091 - 合唱队形 - 动态规划最长子序列
 * 16. Project Euler 357 - Prime generating integers - 数论相关
 * 17. 洛谷 P1208 - 混合牛奶 - 经典贪心问题
 * 18. 牛客网 NC140 - 排序 - 各种排序算法实现
 */
int findMinMoves(int arr[], int n) {
    int sum = 0;
    for (int i = 0; i < n; i++) {
        sum += arr[i];
    }
    if (sum % n != 0) {
        return -1;
    }
    int avg = sum / n; // 每台洗衣机最终要求的衣服数量一定是平均值
    int leftSum = 0; // 左侧累加和
    int leftNeed = 0; // 左边还需要多少件衣服
    int rightNeed = 0;// 右边还需要多少件衣服
    int bottleNeck = 0;// 每一步的瓶颈
    int ans = 0;
    
    for (int i = 0; i < n; leftSum += arr[i], i++) {
        leftNeed = i * avg - leftSum;
        rightNeed = (n - i - 1) * avg - (sum - leftSum - arr[i]);
        
        if (leftNeed > 0 && rightNeed > 0) {
            bottleNeck = leftNeed + rightNeed;
        } else {
            int absLeftNeed = leftNeed < 0 ? -leftNeed : leftNeed;
            int absRightNeed = rightNeed < 0 ? -rightNeed : rightNeed;
            bottleNeck = absLeftNeed > absRightNeed ? absLeftNeed : absRightNeed;
        }
        
        if (ans < bottleNeck) {
            ans = bottleNeck;
        }
    }
    return ans;
}

// 测试函数，返回结果用于验证
int test_findMinMoves() {
    // 测试用例1: 基本情况
    int machines1[] = {1, 0, 5};
    int result1 = findMinMoves(machines1, 3);
    
    // 测试用例2: 无法平均分配
    int machines2[] = {0, 3, 0};
    int result2 = findMinMoves(machines2, 3);
    
    // 测试用例3: 已经平均分配
    int machines3[] = {2, 2, 2};
    int result3 = findMinMoves(machines3, 3);
    
    // 测试用例4: 复杂情况
    int machines4[] = {4, 0, 0, 4};
    int result4 = findMinMoves(machines4, 4);
    
    // 返回最后一个测试用例的结果
    return result4;
}

// 主函数，用于编译和运行测试
int main() {
    return test_findMinMoves();
}