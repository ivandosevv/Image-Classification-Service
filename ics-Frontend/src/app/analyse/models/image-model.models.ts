import {Timestamp} from "rxjs";
import {Tag} from "./tag-model.models";
import {Connection} from "./connection-model.model";

export interface Image {
  id?: number;
  url: string;
  addedOn: string;
  service: string;
  width: number;
  height: number;
  connections: Connection[];
}
