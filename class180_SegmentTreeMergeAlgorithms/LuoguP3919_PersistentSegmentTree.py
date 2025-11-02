#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
洛谷 P3919 【模板】可持久化数组（可持久化线段树/平衡树）
题目链接: https://www.luogu.com.cn/problem/P3919

题目描述:
如题，你需要维护这样的一个长度为N的数组，支持如下几种操作：
1. 在某个历史版本上修改某一个位置上的值
2. 访问某个历史版本上的某一个位置的值

输入格式:
第一行包含两个正整数N,M，分别表示数组的长度和操作的个数。
第二行包含N个整数，第i个整数表示初始状态下数组第i项的值。
接下来M行每行包含若干个整数，表示一个操作。

操作格式如下：
1. C i x v：在版本x的基础上，将位置i的值修改为v，并生成新版本
2. Q i x：查询版本x中位置i的值

输出格式:
对于每个Q操作，输出一行一个整数表示答案。

时间复杂度: 
- 建树: O(n)
- 单点修改: O(log n)
- 单点查询: O(log n)

空间复杂度: O(n log n)

解题思路:
使用可持久化线段树（主席树）来解决这个问题：
1. 可持久化线段树是一种可以保存历史版本的数据结构
2. 每次修改时，只创建从根到修改节点路径上的新节点，其余节点共享
3. 这样可以大大节省空间，同时保留所有历史版本
4. 查询时，根据版本号找到对应的根节点，然后进行常规线段树查询
"""

class PersistentSegmentTreeNode:
    """可持久化线段树节点"""
    def __init__(self, left=None, right=None, val=0):
        self.left = left   # 左子节点
        self.right = right # 右子节点
        self.val = val     # 节点值（对于叶子节点，存储数组元素；对于非叶子节点，可以存储区间信息）

class PersistentSegmentTree:
    """可持久化线段树（主席树）"""
    def __init__(self, size):
        """
        初始化可持久化线段树
        
        Args:
            size: 数组大小
        """
        self.n = size
        self.roots = []  # 存储各个版本的根节点
        
    def build(self, arr, l, r):
        """
        构建初始版本的线段树
        
        Args:
            arr: 初始数组
            l: 当前区间左端点
            r: 当前区间右端点
            
        Returns:
            构建完成的节点
        """
        node = PersistentSegmentTreeNode()
        if l == r:
            node.val = arr[l]
        else:
            mid = (l + r) >> 1
            node.left = self.build(arr, l, mid)
            node.right = self.build(arr, mid + 1, r)
        return node
    
    def update(self, pre_root, idx, val, l, r):
        """
        在前一个版本的基础上更新指定位置的值，生成新版本
        
        Args:
            pre_root: 前一个版本的根节点
            idx: 要更新的位置（0-indexed）
            val: 新的值
            l: 当前区间左端点
            r: 当前区间右端点
            
        Returns:
            新版本的根节点
        """
        node = PersistentSegmentTreeNode()
        if l == r:
            node.val = val
        else:
            mid = (l + r) >> 1
            # 根据更新位置决定哪一边需要新建节点
            if idx <= mid:
                # 更新左子树，右子树可以复用
                node.left = self.update(pre_root.left, idx, val, l, mid)
                node.right = pre_root.right
            else:
                # 更新右子树，左子树可以复用
                node.left = pre_root.left
                node.right = self.update(pre_root.right, idx, val, mid + 1, r)
            # 更新当前节点的值（如果需要的话）
            # 这里是叶子节点才存储值，非叶子节点不存储值
        return node
    
    def query(self, root, idx, l, r):
        """
        查询指定版本中指定位置的值
        
        Args:
            root: 版本的根节点
            idx: 要查询的位置（0-indexed）
            l: 当前区间左端点
            r: 当前区间右端点
            
        Returns:
            位置idx处的值
        """
        if l == r:
            return root.val
        mid = (l + r) >> 1
        if idx <= mid:
            return self.query(root.left, idx, l, mid)
        else:
            return self.query(root.right, idx, mid + 1, r)

def main():
    """
    主函数，处理输入输出
    """
    # 为了演示，我们使用示例输入
    # 实际使用时应该用: 
    # import sys
    # lines = sys.stdin.read().split('\n')
    lines = [
        "5 9",
        "5 4 3 2 1",
        "1 1 0 3",    # 在版本0的基础上，将位置1的值修改为3
        "2 1 1",      # 查询版本1中位置1的值
        "1 2 1 5",    # 在版本1的基础上，将位置2的值修改为5
        "1 3 2 4",    # 在版本2的基础上，将位置3的值修改为4
        "2 2 3",      # 查询版本3中位置2的值
        "2 3 3",      # 查询版本3中位置3的值
        "1 4 2 2",    # 在版本2的基础上，将位置4的值修改为2
        "2 3 4",      # 查询版本4中位置3的值
        "2 4 4"       # 查询版本4中位置4的值
    ]
    
    # 解析输入
    n, m = map(int, lines[0].split())
    arr = list(map(int, lines[1].split()))
    
    # 初始化可持久化线段树
    pst = PersistentSegmentTree(n)
    
    # 构建初始版本（版本0）
    root0 = pst.build(arr, 0, n - 1)
    pst.roots.append(root0)
    
    # 处理操作
    results = []
    for i in range(2, 2 + m):
        operation = lines[i].split()
        if operation[0] == '1':
            # 修改操作：C i x v
            # 在版本x的基础上，将位置i的值修改为v
            _, i_pos, x_ver, v_val = operation
            i_pos, x_ver, v_val = int(i_pos) - 1, int(x_ver), int(v_val)  # 转换为0-indexed
            
            # 检查版本号是否有效
            if x_ver >= len(pst.roots):
                print(f"错误：版本 {x_ver} 不存在，当前只有 {len(pst.roots)} 个版本")
                continue
            
            # 在版本x_ver的基础上更新位置i_pos的值为v_val
            new_root = pst.update(pst.roots[x_ver], i_pos, v_val, 0, n - 1)
            pst.roots.append(new_root)
        else:
            # 查询操作：Q i x
            # 查询版本x中位置i的值
            _, i_pos, x_ver = operation
            i_pos, x_ver = int(i_pos) - 1, int(x_ver)  # 转换为0-indexed
            
            # 检查版本号是否有效
            if x_ver >= len(pst.roots):
                print(f"错误：版本 {x_ver} 不存在，当前只有 {len(pst.roots)} 个版本")
                continue
            
            # 查询版本x_ver中位置i_pos的值
            result = pst.query(pst.roots[x_ver], i_pos, 0, n - 1)
            results.append(result)
    
    # 输出查询结果
    for result in results:
        print(result)


# 测试代码
if __name__ == "__main__":
    # 创建可持久化线段树
    print("可持久化线段树（主席树）测试")
    
    # 运行主函数示例
    main()