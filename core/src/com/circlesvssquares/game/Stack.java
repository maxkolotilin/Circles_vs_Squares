package com.circlesvssquares.game;

import java.util.NoSuchElementException;

/**
 * Created by maximKa on 24.02.16.
 */
public class Stack<T> {
    private T[] stack;
    private int size = 0;

    public Stack(int capacity)
    {
        stack = (T[])new Object[capacity];
    }

    public Stack() {
        this(10);
    }

    public int Capacity()
    {
        return stack.length;
    }

    public int Size()
    {
        return size;
    }

    public void Push(T item)
    {
        if (size == stack.length) {
            Resize((int)(stack.length * 1.5));
        }
        stack[size++] = item;
    }

    public T Peek()
    {
        if (size > 0)
            return stack[size - 1];

        throw new NoSuchElementException("Try to peek from empty stack");
    }

    public T Pop()
    {
        if (size > 0) {
            T value = stack[--size];
            stack[size] = null;

            return value;
        }

        throw new NoSuchElementException("Try to pop from empty stack");
    }

    public void Erase()
    {
        size = 0;
    }

    public void Resize(int newCapacity)
    {
        T[] newStack = (T[])new Object[newCapacity];
        size = Math.min(newCapacity, size);
        for (int i = 0; i < size; ++i) {
            newStack[i] = stack[i];
        }
        stack = newStack;
    }

    public void TrimToFit()
    {
        Resize(size);
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder("[ ");
        for (int i = 0; i < size; ++i) {
            str.append(stack[i]);
            str.append(" ");
        }
        str.append("]");

        return str.toString();
    }
}
