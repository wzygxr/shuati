"""
Codeforces 380C - Sereja and Brackets
题目：括号匹配查询
来源：Codeforces
网址：https://codeforces.com/problemset/problem/380/C

给定一个括号字符串，支持区间查询：查询区间内最多能匹配多少对括号

解题思路：
使用线段树维护每个区间的信息：
1. 左侧未匹配的'('数量
2. 右侧未匹配的')'数量
3. 区间内已匹配的括号对数

合并两个区间时：
1. 新的已匹配对数 = 左区间已匹配对数 + 右区间已匹配对数 + min(左区间右侧未匹配')', 右区间左侧未匹配'(')
2. 新的左侧未匹配'(' = 左区间左侧未匹配'(' + max(0, 右区间左侧未匹配'(' - 左区间右侧未匹配')')
3. 新的右侧未匹配')' = 右区间右侧未匹配')' + max(0, 左区间右侧未匹配')' - 右区间左侧未匹配'(')

时间复杂度：
  - 建树：O(n)
  - 区间查询：O(log n)
空间复杂度：O(n)
"""

class BracketNode:
    def __init__(self, left_unmatched=0, right_unmatched=0, matched_pairs=0):
        self.left_unmatched = left_unmatched      # 左侧未匹配的'('数量
        self.right_unmatched = right_unmatched    # 右侧未匹配的')'数量
        self.matched_pairs = matched_pairs        # 已匹配的括号对数

class SerejaAndBrackets:
    def __init__(self, s):
        """
        初始化线段树
        Args:
            s: 输入的括号字符串
        """
        self.s = s
        self.n = len(s)
        self.tree = [BracketNode() for _ in range(4 * self.n)]
        self._build(0, 0, self.n - 1)
    
    def _merge(self, left, right):
        """
        合并两个节点的信息
        Args:
            left: 左子树节点
            right: 右子树节点
        Returns:
            合并后的节点
        """
        if left is None:
            return right
        if right is None:
            return left
        
        # 计算左右区间可以匹配的括号对数
        new_matched = left.matched_pairs + right.matched_pairs + \
                      min(left.right_unmatched, right.left_unmatched)
        
        # 计算合并后左侧未匹配的'('数量
        new_left_unmatched = left.left_unmatched + \
                            max(0, right.left_unmatched - left.right_unmatched)
        
        # 计算合并后右侧未匹配的')'数量
        new_right_unmatched = right.right_unmatched + \
                             max(0, left.right_unmatched - right.left_unmatched)
        
        return BracketNode(new_left_unmatched, new_right_unmatched, new_matched)
    
    def _build(self, idx, l, r):
        """
        递归构建线段树
        Args:
            idx: 当前节点索引
            l, r: 当前节点表示的区间
        """
        if l == r:
            c = self.s[l]
            if c == '(':
                self.tree[idx] = BracketNode(1, 0, 0)
            elif c == ')':
                self.tree[idx] = BracketNode(0, 1, 0)
            else:
                self.tree[idx] = BracketNode(0, 0, 0)
            return
        
        mid = (l + r) // 2
        self._build(2 * idx + 1, l, mid)
        self._build(2 * idx + 2, mid + 1, r)
        self.tree[idx] = self._merge(self.tree[2 * idx + 1], self.tree[2 * idx + 2])
    
    def query(self, ql, qr):
        """
        区间查询匹配的括号对数
        Args:
            ql, qr: 查询区间
        Returns:
            匹配的括号对数
        """
        if ql < 0 or qr >= self.n or ql > qr:
            raise ValueError("Invalid range")
        result = self._query(0, 0, self.n - 1, ql, qr)
        return result.matched_pairs if result else 0
    
    def _query(self, idx, l, r, ql, qr):
        """
        递归查询
        Args:
            idx: 当前节点索引
            l, r: 当前节点表示的区间
            ql, qr: 查询区间
        Returns:
            查询结果节点
        """
        if ql <= l and r <= qr:
            return self.tree[idx]
        
        mid = (l + r) // 2
        left_result = None
        right_result = None
        
        if ql <= mid:
            left_result = self._query(2 * idx + 1, l, mid, ql, qr)
        if qr > mid:
            right_result = self._query(2 * idx + 2, mid + 1, r, ql, qr)
        
        if left_result is None:
            return right_result
        if right_result is None:
            return left_result
        return self._merge(left_result, right_result)

# 测试代码
if __name__ == "__main__":
    # 测试样例
    s = "()()()"
    st = SerejaAndBrackets(s)
    
    # 查询整个字符串的匹配对数
    print(f'字符串 "{s}" 的匹配对数: {st.query(0, len(s) - 1)}')  # 3
    
    # 查询子区间
    print(f"区间[0,3]的匹配对数: {st.query(0, 3)}")  # 2
    print(f"区间[1,4]的匹配对数: {st.query(1, 4)}")  # 2
    print(f"区间[2,5]的匹配对数: {st.query(2, 5)}")  # 2
    
    # 测试复杂情况
    s2 = "((()))"
    st2 = SerejaAndBrackets(s2)
    print(f'字符串 "{s2}" 的匹配对数: {st2.query(0, len(s2) - 1)}')  # 3
    
    # 测试不匹配情况
    s3 = "((("
    st3 = SerejaAndBrackets(s3)
    print(f'字符串 "{s3}" 的匹配对数: {st3.query(0, len(s3) - 1)}')  # 0
    
    # 测试异常处理
    try:
        st.query(-1, 2)
    except ValueError as e:
        print(f"异常测试: {e}")