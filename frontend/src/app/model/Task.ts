import { State } from './State';

export interface Task {
    id: Number,
    name: String;
    description: String;
    state: State;
  }
  