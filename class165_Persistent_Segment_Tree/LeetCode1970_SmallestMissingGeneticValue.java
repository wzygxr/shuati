package class158;

import java.io.*;
import java.util.*;

/**
 * LeetCode 1970. Smallest Missing Genetic Value in Each Subtree
 * 
 * 题目来源：LeetCode
 * 题目链接：https://leetcode.com/problems/smallest-missing-genetic-value-in-each-subtree/
 * 
 * 题目描述:
 * 给你一棵根节点为0的家族树，树中节点从0到n-1编号，parents[i]是节点i的父节点。
 * 每个节点有一个基因值genes[i]。对于每个节点，求出以其为根的子树中缺失的最小基因值。
 * 
 * 解题思路:
 * 使用可持久化线段树（主席树）解决子树Mex问题。
 * 1. 通过DFS遍历树，为每个节点建立主席树
 * 2. 对于每个节点，将其子树中的所有基因值插入到主席树中
 * 3. 查询Mex即为查询区间内未出现的最小自然数
 * 
 * 时间复杂度: O(n log n)
 * 空间复杂度: O(n log n)
 * 
 * 示例:
 * 输入:
 * parents = [-1,0,0,2], genes = [1,2,3,4]
 * 输出:
 * [5,1,1,1]
 * 
 * 解释:
 * - 节点0的子树包含基因值1,2,3,4，缺失的最小值为5
 * - 节点1的子树只包含基因值2，缺失的最小值为1
 * - 节点2的子树包含基因值3,4，缺失的最小值为1
 * - 节点3的子树只包含基因值4，缺失的最小值为1
 */
public class LeetCode1970_SmallestMissingGeneticValue {
    static final int MAXN = 100010;
    
    // 邻接表存储树
    static int[] head = new int[MAXN];
    static int[] to = new int[MAXN];
    static int[] next = new int[MAXN];
    static int cnt = 0;
    
    // 基因值
    static int[] genes = new int[MAXN];
    
    // 可持久化线段树需要
    static int[] root = new int[MAXN];
    static int[] left = new int[MAXN * 20];
    static int[] right = new int[MAXN * 20];
    static int[] sum = new int[MAXN * 20];
    static int treeCnt = 0;
    
    // DFS需要
    static int[] dfn = new int[MAXN];  // DFS序
    static int[] end = new int[MAXN];  // 子树结束位置
    static int timestamp = 0;
    
    /**
     * 添加边
     * @param u 起点
     * @param v 终点
     */
    static void addEdge(int u, int v) {
        next[++cnt] = head[u];
        to[cnt] = v;
        head[u] = cnt;
    }
    
    /**
     * 构建空线段树
     * @param l 区间左端点
     * @param r 区间右端点
     * @return 根节点编号
     */
    static int build(int l, int r) {
        int rt = ++treeCnt;
        sum[rt] = 0;
        if (l < r) {
            int mid = (l + r) / 2;
            left[rt] = build(l, mid);
            right[rt] = build(mid + 1, r);
        }
        return rt;
    }
    
    /**
     * 在线段树中插入一个值
     * @param pos 要插入的位置
     * @param l 区间左端点
     * @param r 区间右端点
     * @param pre 前一个版本的节点编号
     * @return 新节点编号
     */
    static int insert(int pos, int l, int r, int pre) {
        int rt = ++treeCnt;
        left[rt] = left[pre];
        right[rt] = right[pre];
        sum[rt] = sum[pre] + 1;
        
        if (l < r) {
            int mid = (l + r) / 2;
            if (pos <= mid) {
                left[rt] = insert(pos, l, mid, left[rt]);
            } else {
                right[rt] = insert(pos, mid + 1, r, right[rt]);
            }
        }
        return rt;
    }
    
    /**
     * 查询区间Mex
     * @param l 查询区间左端点
     * @param r 查询区间右端点
     * @param u 前一个版本的根节点
     * @param v 当前版本的根节点
     * @return Mex值
     */
    static int queryMex(int l, int r, int u, int v) {
        if (l == r) {
            return l;
        }
        int mid = (l + r) / 2;
        // 计算左子树中数的个数
        int leftCount = sum[left[v]] - sum[left[u]];
        // 如果左子树中数的个数等于区间长度，说明左子树满
        if (leftCount == mid - l + 1) {
            // Mex在右子树中
            return queryMex(mid + 1, r, right[u], right[v]);
        } else {
            // Mex在左子树中
            return queryMex(l, mid, left[u], left[v]);
        }
    }
    
    /**
     * DFS遍历树并构建主席树
     * @param u 当前节点
     * @param pre 父节点
     */
    static void dfs(int u, int pre) {
        dfn[u] = ++timestamp;
        
        // 处理子节点
        for (int i = head[u]; i > 0; i = next[i]) {
            int v = to[i];
            if (v != pre) {
                dfs(v, u);
            }
        }
        
        end[u] = timestamp;
    }
    
    public static int[] smallestMissingValueSubtree(int[] parents, int[] genes) {
        int n = parents.length;
        cnt = 0;
        treeCnt = 0;
        timestamp = 0;
        
        // 初始化
        Arrays.fill(head, 0);
        
        // 建立邻接表
        for (int i = 1; i < n; i++) {
            addEdge(parents[i], i);
            addEdge(i, parents[i]);
        }
        
        // 复制基因值
        System.arraycopy(genes, 0, LeetCode1970_SmallestMissingGeneticValue.genes, 0, n);
        
        // DFS遍历
        dfs(0, -1);
        
        // 构建主席树
        root[0] = build(1, n);
        for (int i = 1; i <= n; i++) {
            root[i] = insert(genes[i - 1], 1, n, root[i - 1]);
        }
        
        // 计算答案
        int[] ans = new int[n];
        for (int i = 0; i < n; i++) {
            ans[i] = queryMex(1, n, root[dfn[i] - 1], root[end[i]]);
        }
        
        return ans;
    }
    
    public static void main(String[] args) {
        // 测试用例
        int[] parents = {-1, 0, 0, 2};
        int[] genes = {1, 2, 3, 4};
        int[] result = smallestMissingValueSubtree(parents, genes);
        
        System.out.println(Arrays.toString(result)); // [5, 1, 1, 1]
    }
}