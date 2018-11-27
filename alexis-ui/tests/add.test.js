const add = (a, b) => a + b;

test('should add to numbers', () => {
    const result = add(3, 2);

    if (result !== 5) {
        throw new Error(`You add 3 and 2. The result was ${result}. Expect 5`);
    }
});

