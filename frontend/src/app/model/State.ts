export class State {
    static readonly TODO = new State(1, "TODO");
    static readonly IN_PROGRESS = new State(2, "IN_PROGRESS");
    static readonly DONE = new State(3, "DONE");

    private constructor(private readonly id: number, private readonly name: string) {
    }

    toString() {
      return this.name;
    }

    getId() {
      return this.id;
    }
  }
  