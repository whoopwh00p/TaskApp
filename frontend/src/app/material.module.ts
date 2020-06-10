import { NgModule } from '@angular/core';

import { MatDialogModule } from '@angular/material/dialog';
import {MatGridListModule} from '@angular/material/grid-list';
import { FormsModule } from '@angular/forms';
import {MatListModule} from '@angular/material/list';
@NgModule({
  exports: [FormsModule, MatDialogModule, MatGridListModule,MatListModule]
})
export class MaterialModule {}