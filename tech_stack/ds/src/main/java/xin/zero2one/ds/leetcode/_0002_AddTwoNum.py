#!/usr/bin/env python
# -*- coding: utf-8 -*-
"""
    @Time    : 2019/4/22 18:59
    @Author    : Geoffrey
    @File      : add_two_num.py
    @Software   : PyCharm
"""
# from ListNode import ListNode


class AddTwoNum:

    def add_two_num(self, l1: ListNode, l2: ListNode) -> ListNode:
        if l1 is None:
            return l2
        if l2 is None:
            return l1
        head = ListNode(-1)
        node = head
        carry = 0
        while l1 or l2:
            v1 = 0
            v2 = 0
            if l1:
                v1 = l1.val
                l1 = l1.next
            if l2:
                v2 = l2.val
                l2 = l2.next
            sum = v1 + v2 + carry
            v = sum % 10
            carry = sum // 10
            node.next = ListNode(v)
            node = node.next
        if carry > 0:
            node.next = ListNode(carry)
        return head.next

    def build_list_node(self, *values):
        head = ListNode(-1)
        node = head
        for val in values:
            node.next = ListNode(val)
            node = node.next

        return head.next

    def traverse_list_node(self, list_node):
        while list_node:
            print(str(list_node.val), end=" ")
            list_node = list_node.next
        print()

def main():
    obj = AddTwoNum();
    l1 = obj.build_list_node(9, 9, 9)
    l2 = obj.build_list_node(9, 9, 9)
    obj.traverse_list_node(l1)
    obj.traverse_list_node(l2)
    sum = obj.add_two_num(l1, l2)
    obj.traverse_list_node(sum)


if __name__ == '__main__':
    main()


class ListNode:
    def __init__(self, x):
        self.val = x
        self.next = None
