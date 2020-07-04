import { State } from './State';
import { User } from './User';

export interface Task {
    id: Number,
    name: String;
    description: String;
    state: State;
    assigneeId: String;
    assigneeName: String;
  }
  