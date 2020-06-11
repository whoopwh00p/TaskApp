export class State {
    static readonly TODO = new State(1, "Todo");
    static readonly IN_PROGRESS = new State(2, "in progress");
    static readonly DONE = new State(3, "done");

    private constructor(private readonly id: number, private readonly name: string) {
    }

    toString() {
      return this.name;
    }

    getId() {
      return this.id;
    }
  }
  