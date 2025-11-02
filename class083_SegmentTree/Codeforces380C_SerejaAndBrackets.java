import java.util.*;

/**
 * Codeforces 380C - Sereja and Brackets
 * 题目：括号匹配查询
 * 来源：Codeforces
 * 网址：https://codeforces.com/problemset/problem/380/C
 * 
 * 给定一个括号字符串，支持区间查询：查询区间内最多能匹配多少对括号
 * 
 * 解题思路：
 * 使用线段树维护每个区间的信息：
 * 1. 左侧未匹配的'('数量
 * 2. 右侧未匹配的')'数量
 * 3. 区间内已匹配的括号对数
 * 
 * 合并两个区间时：
 * 1. 新的已匹配对数 = 左区间已匹配对数 + 右区间已匹配对数 + min(左区间右侧未匹配')', 右区间左侧未匹配'(')
 * 2. 新的左侧未匹配'(' = 左区间左侧未匹配'(' + max(0, 右区间左侧未匹配'(' - 左区间右侧未匹配')')
 * 3. 新的右侧未匹配')' = 右区间右侧未匹配')' + max(0, 左区间右侧未匹配')' - 右区间左侧未匹配'(')
 * 
 * 时间复杂度：
 *   - 建树：O(n)
 *   - 区间查询：O(log n)
 * 空间复杂度：O(n)
 */

class BracketNode {
    int leftUnmatched;   // 左侧未匹配的'('数量
    int rightUnmatched;  // 右侧未匹配的')'数量
    int matchedPairs;    // 已匹配的括号对数
    
    BracketNode(int left, int right, int matched) {
        this.leftUnmatched = left;
        this.rightUnmatched = right;
        this.matchedPairs = matched;
    }
}

public class Codeforces380C_SerejaAndBrackets {
    private BracketNode[] tree;
    private String s;
    private int n;
    
    public Codeforces380C_SerejaAndBrackets(String s) {
        this.s = s;
        this.n = s.length();
        this.tree = new BracketNode[4 * n];
        build(0, 0, n - 1);
    }
    
    private BracketNode merge(BracketNode left, BracketNode right) {
        if (left == null) return right;
        if (right == null) return left;
        
        // 计算左右区间可以匹配的括号对数
        int newMatched = left.matchedPairs + right.matchedPairs + 
                         Math.min(left.rightUnmatched, right.leftUnmatched);
        
        // 计算合并后左侧未匹配的'('数量
        int newLeftUnmatched = left.leftUnmatched + 
                              Math.max(0, right.leftUnmatched - left.rightUnmatched);
        
        // 计算合并后右侧未匹配的')'数量
        int newRightUnmatched = right.rightUnmatched + 
                               Math.max(0, left.rightUnmatched - right.leftUnmatched);
        
        return new BracketNode(newLeftUnmatched, newRightUnmatched, newMatched);
    }
    
    private void build(int idx, int l, int r) {
        if (l == r) {
            char c = s.charAt(l);
            if (c == '(') {
                tree[idx] = new BracketNode(1, 0, 0);
            } else if (c == ')') {
                tree[idx] = new BracketNode(0, 1, 0);
            } else {
                tree[idx] = new BracketNode(0, 0, 0);
            }
            return;
        }
        
        int mid = (l + r) / 2;
        build(2 * idx + 1, l, mid);
        build(2 * idx + 2, mid + 1, r);
        tree[idx] = merge(tree[2 * idx + 1], tree[2 * idx + 2]);
    }
    
    public int query(int ql, int qr) {
        return query(0, 0, n - 1, ql, qr).matchedPairs;
    }
    
    private BracketNode query(int idx, int l, int r, int ql, int qr) {
        if (ql <= l && r <= qr) {
            return tree[idx];
        }
        
        int mid = (l + r) / 2;
        BracketNode leftResult = null, rightResult = null;
        
        if (ql <= mid) {
            leftResult = query(2 * idx + 1, l, mid, ql, qr);
        }
        if (qr > mid) {
            rightResult = query(2 * idx + 2, mid + 1, r, ql, qr);
        }
        
        if (leftResult == null) return rightResult;
        if (rightResult == null) return leftResult;
        return merge(leftResult, rightResult);
    }
    
    public static void main(String[] args) {
        // 测试样例
        String s = "()()()";
        Codeforces380C_SerejaAndBrackets st = new Codeforces380C_SerejaAndBrackets(s);
        
        // 查询整个字符串的匹配对数
        System.out.println("字符串 \"" + s + "\" 的匹配对数: " + st.query(0, s.length() - 1)); // 3
        
        // 查询子区间
        System.out.println("区间[0,3]的匹配对数: " + st.query(0, 3)); // 2
        System.out.println("区间[1,4]的匹配对数: " + st.query(1, 4)); // 2
        System.out.println("区间[2,5]的匹配对数: " + st.query(2, 5)); // 2
        
        // 测试复杂情况
        String s2 = "((()))";
        Codeforces380C_SerejaAndBrackets st2 = new Codeforces380C_SerejaAndBrackets(s2);
        System.out.println("字符串 \"" + s2 + "\" 的匹配对数: " + st2.query(0, s2.length() - 1)); // 3
        
        // 测试不匹配情况
        String s3 = "(((";
        Codeforces380C_SerejaAndBrackets st3 = new Codeforces380C_SerejaAndBrackets(s3);
        System.out.println("字符串 \"" + s3 + "\" 的匹配对数: " + st3.query(0, s3.length() - 1)); // 0
    }
}