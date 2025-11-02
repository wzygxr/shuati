#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
测试所有AVL树实现的脚本
"""

def test_code01_avl():
    """测试基础AVL树实现"""
    print("测试 Code01_AVL.py ...")
    import Code01_AVL
    
    # 创建AVL树
    avl = Code01_AVL.AVLTree()
    
    # 插入一些数据
    for i in [10, 20, 30, 40, 50, 25]:
        avl.insert_key(i)
    
    # 测试各种操作
    print(f"30的排名: {avl.get_rank(30)}")
    print(f"第3小的数: {avl.get_select(3)}")
    print(f"25的前驱: {avl.get_predecessor(25)}")
    print(f"25的后继: {avl.get_successor(25)}")
    
    # 删除一个元素
    avl.delete_key(30)
    print(f"删除30后，30的排名: {avl.get_rank(30)}")
    print(f"删除30后，第3小的数: {avl.get_select(3)}")
    
    print("Code01_AVL.py 测试完成!\n")


def test_code02_reconstruction_queue():
    """测试重建队列实现"""
    print("测试 Code02_ReconstructionQueue.py ...")
    import Code02_ReconstructionQueue
    
    # 测试用例
    people = [[7,0],[4,4],[7,1],[5,0],[6,1],[5,2]]
    result = Code02_ReconstructionQueue.reconstruct_queue(people)
    print(f"输入: {people}")
    print(f"输出: {result}")
    
    # 验证结果
    def validate_queue(queue):
        for i in range(len(queue)):
            height, requirement = queue[i]
            count = 0
            for j in range(i):
                if queue[j][0] >= height:
                    count += 1
            if count != requirement:
                return False
        return True
    
    print(f"验证结果: {validate_queue(result)}")
    print("Code02_ReconstructionQueue.py 测试完成!\n")


def test_followup1():
    """测试数据加强版实现"""
    print("测试 FollowUp1.py ...")
    import FollowUp1
    
    # 创建AVL树
    avl = FollowUp1.AVLTree()
    
    # 插入一些数据
    for i in [10, 20, 30, 40, 50, 25]:
        avl.insert_key(i)
    
    # 测试各种操作
    print(f"30的排名: {avl.get_rank(30)}")
    print(f"第3小的数: {avl.get_select(3)}")
    print(f"25的前驱: {avl.get_predecessor(25)}")
    print(f"25的后继: {avl.get_successor(25)}")
    
    # 删除一个元素
    avl.delete_key(30)
    print(f"删除30后，30的排名: {avl.get_rank(30)}")
    print(f"删除30后，第3小的数: {avl.get_select(3)}")
    
    print("FollowUp1.py 测试完成!\n")


def test_followup2():
    """测试数据加强版实现2"""
    print("测试 FollowUp2.py ...")
    import FollowUp2
    
    # 创建AVL树
    avl = FollowUp2.AVLTree()
    
    # 插入一些数据
    for i in [10, 20, 30, 40, 50, 25]:
        avl.insert_key(i)
    
    # 测试各种操作
    print(f"30的排名: {avl.get_rank(30)}")
    print(f"第3小的数: {avl.get_select(3)}")
    print(f"25的前驱: {avl.get_predecessor(25)}")
    print(f"25的后继: {avl.get_successor(25)}")
    
    # 删除一个元素
    avl.delete_key(30)
    print(f"删除30后，30的排名: {avl.get_rank(30)}")
    print(f"删除30后，第3小的数: {avl.get_select(3)}")
    
    print("FollowUp2.py 测试完成!\n")


def main():
    """主函数"""
    print("=" * 50)
    print("AVL树实现测试")
    print("=" * 50)
    
    try:
        test_code01_avl()
        test_code02_reconstruction_queue()
        test_followup1()
        test_followup2()
        
        print("=" * 50)
        print("所有测试完成!")
        print("=" * 50)
        
    except Exception as e:
        print(f"测试过程中出现错误: {e}")
        import traceback
        traceback.print_exc()


if __name__ == "__main__":
    main()