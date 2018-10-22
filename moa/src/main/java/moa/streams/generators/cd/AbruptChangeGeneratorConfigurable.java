/*
 *    AbruptChangeGenerator.java
 *    Copyright (C) 2012 University of Waikato, Hamilton, New Zealand
 *    @author Albert Bifet (abifet@cs.waikato.ac.nz)
 *
 *    This program is free software; you can redistribute it and/or modify
 *    it under the terms of the GNU General Public License as published by
 *    the Free Software Foundation; either version 3 of the License, or
 *    (at your option) any later version.
 *
 *    This program is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU General Public License for more details.
 *
 *    You should have received a copy of the GNU General Public License
 *    along with this program. If not, see <http://www.gnu.org/licenses/>.
 *
 */
package moa.streams.generators.cd;

import com.github.javacliparser.FloatOption;

public class AbruptChangeGeneratorConfigurable extends AbstractConceptDriftGenerator {

    public FloatOption initialErrorLevel = new FloatOption(
            "initialErrorLevel", 's', "Error level in first concept",
            0.2, 0.0, 1.0);

    public FloatOption finalErrorLevel = new FloatOption(
            "finalErrorLevel", 'd', "Error level in first concept",
            0.8, 0.0, 1.0);

    @Override
    protected double nextValue() {
        double res;
        double t = this.numInstances % this.period;
        this.change = (t == (this.period / 2));
        res = (t < (this.period / 2)) ? initialErrorLevel.getValue() : finalErrorLevel.getValue();
        return res;
    }
}